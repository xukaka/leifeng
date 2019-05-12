package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.story.DiaryContentDao;
import io.renren.modules.app.dao.story.DiaryDao;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.entity.ParagraphTypeEnum;
import io.renren.modules.app.entity.story.DiaryContentEntity;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.form.DiaryContentForm;
import io.renren.modules.app.form.DiaryForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.DiaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

@Service
public class DiaryServiceImpl extends ServiceImpl<DiaryDao, DiaryEntity> implements DiaryService {
    private final static Logger logger = LoggerFactory.getLogger(DiaryServiceImpl.class);

    @Autowired
    private DiaryDao diaryDao;

    @Autowired
    private DiaryContentDao diaryContentDao;

    @Override
    @Transactional
    public void createDiary(Long memberId,DiaryForm form) {
        ValidatorUtils.validateEntity(form);
        DiaryEntity diary = new DiaryEntity();
        BeanUtils.copyProperties(form, diary);
        diary.setCreatorId(memberId);
        diary.setCreateTime(DateUtils.now());
        this.insert(diary);
        addDiaryContent(diary.getId(),form.getContents());

    }


    //添加日记内容
    private void addDiaryContent(Long diaryId,List<DiaryContentForm> contents){
        long createTime = DateUtils.now();
        for (DiaryContentForm form:contents){
            DiaryContentEntity content= new DiaryContentEntity();
            BeanUtils.copyProperties(form, content);
            content.setDiaryId(diaryId);
            content.setCreateTime(createTime);
            diaryContentDao.insert(content);
        }
    }


    @Override
    public DiaryDto getDiary(Long id) {
       return baseMapper.getDiary(id);
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
                //            contents.sort((o1, o2)->o1.getParagraphSort()-o2.getParagraphSort());
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
        List<DiaryDto> diarys = baseMapper.getMyDiarys(creatorId, page);
        if (CollectionUtils.isEmpty(diarys)) {
            return new PageUtils<>();
        }
        setAvatarAndDescription(diarys);
        int total = baseMapper.myDiaryCount(creatorId);
        return new PageUtils<>(diarys, total, page.getPageSize(), page.getCurrPage());
    }
}
