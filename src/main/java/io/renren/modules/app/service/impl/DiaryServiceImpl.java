package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.dao.diary.DiaryContentDao;
import io.renren.modules.app.dao.diary.DiaryDao;
import io.renren.modules.app.dao.member.MemberFollowDao;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.entity.LikeTypeEnum;
import io.renren.modules.app.entity.ParagraphTypeEnum;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.member.MemberFollowEntity;
import io.renren.modules.app.entity.story.DiaryContentEntity;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.form.DiaryContentForm;
import io.renren.modules.app.form.DiaryForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.DiaryService;
import io.renren.modules.app.service.LikeService;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.service.WechatSecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Service
public class DiaryServiceImpl extends ServiceImpl<DiaryDao, DiaryEntity> implements DiaryService {
    private final static Logger LOG = LoggerFactory.getLogger(DiaryServiceImpl.class);

    @Resource
    private DiaryContentDao diaryContentDao;
    @Resource
    private LikeService likeService;

    @Resource
    private MemberService memberService;
    @Resource
    private RabbitMqHelper rabbitMqHelper;

    @Resource
    private MemberFollowDao memberFollowDao;
    @Autowired
    private WechatSecurityCheck wechatSecurityCheck;


    @Override
    @Transactional
    public void createDiary(Long memberId,DiaryForm form) {
        LOG.debug("create diary params:memberId={},form={}",memberId,form);
        ValidatorUtils.validateEntity(form);
        DiaryEntity diary = new DiaryEntity();
        BeanUtils.copyProperties(form, diary);

        wechatSecurityCheck.checkMsgSecurity(diary.getTitle());
        diary.setCreatorId(memberId);
        diary.setCreateTime(DateUtils.now());
        this.insert(diary);
        addDiaryContent(diary.getId(),form.getContents());
        //非私密日记
        if (!diary.getPrivate()){
            ThreadPoolUtils.execute(() -> {
                //推送消息给关注我的所有人
                rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_DYNAMIC, ImMessageUtils.getDynamicMsg(memberId, "diary", diary.getId()));
            });
        }

    }


    //添加日记内容
    private void addDiaryContent(Long diaryId,List<DiaryContentForm> contents){
        checkDiaryContentSecurity(contents);
        long createTime = DateUtils.now();
        for (DiaryContentForm form:contents){
            DiaryContentEntity content= new DiaryContentEntity();
            BeanUtils.copyProperties(form, content);
            content.setDiaryId(diaryId);
            content.setCreateTime(createTime);
            diaryContentDao.insert(content);
        }
    }

    private void checkDiaryContentSecurity(List<DiaryContentForm> contents){
        StringBuilder contentMsg = new StringBuilder();
        for (DiaryContentForm form:contents){
            contentMsg.append(form.getParagraph());
        }
        wechatSecurityCheck.checkMsgSecurity(contentMsg.toString());
    }

    @Override
    public DiaryDto getDiary(Long curMemberId,Long id) {
        LOG.debug("get diary params:curMemberId={},id={}",curMemberId,id);
        DiaryDto diary =baseMapper.getDiary(id);
        if (diary!=null){
            //是否点赞
            boolean isLiked = likeService.existsLiked(id, LikeTypeEnum.diary,curMemberId);
            diary.setLiked(isLiked);
        }

        ThreadPoolUtils.execute(()->{
            //浏览数+1
            incViewCount(id,1);
        });

       return diary;


    }

    @Override
    public PageUtils<DiaryDto> getDiarys(PageWrapper page) {
        List<DiaryDto> diarys = baseMapper.getDiarys( page);
        if (CollectionUtils.isEmpty(diarys)) {
            return new PageUtils<>();
        }

        setAvatarAndDescription(diarys);
        int total = baseMapper.count();
        return new PageUtils<>(diarys, total, page.getPageSize(), page.getCurrPage());
    }

    //设置列表头像和描述:第一个text段落作为描述信息，第一个image作为头像
    private void setAvatarAndDescription(List<DiaryDto> diarys) {
        for (DiaryDto diary:diarys){
            List<DiaryContentEntity> contents = diary.getContents();
            if (!CollectionUtils.isEmpty(contents)){
                //对日记内容列表排序
                 contents.sort(Comparator.comparingInt(DiaryContentEntity::getParagraphSort));
                //设置描述
                for (DiaryContentEntity content:contents ){
                    if (content.getType()==ParagraphTypeEnum.text){
                        diary.setDescription(content.getParagraph());
                        break;
                    }
                }
                //设置头像
                for (DiaryContentEntity content:contents ){
                    if (content.getType()==ParagraphTypeEnum.image){
                        diary.setAvatar(content.getParagraph());
                        break;
                    }
                }
            }
            //返回数据不需要显示内容列表
            diary.setContents(null);
        }
    }

    @Override
    public PageUtils<DiaryDto> getMyDiarys(Long creatorId,PageWrapper page) {
        LOG.debug("get my diarys params:creatorId={},page={}",creatorId,page);
        List<DiaryDto> diarys = baseMapper.getMyDiarys(creatorId, page);
        if (CollectionUtils.isEmpty(diarys)) {
            return new PageUtils<>();
        }
        setAvatarAndDescription(diarys);
        int total = baseMapper.myDiaryCount(creatorId);
        return new PageUtils<>(diarys, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void incLikeCount(Long diaryId,Integer inc){
        baseMapper.incLikeCount(diaryId,inc);
    }


    @Override
    public void incCommentCount(Long diaryId, Integer inc) {
        baseMapper.incCommentCount(diaryId, inc);
    }


    @Override
    public void incViewCount(Long diaryId, Integer inc) {
        baseMapper.incViewCount(diaryId, inc);
    }

}
