package io.renren.modules.app.service.impl;

import io.renren.modules.app.entity.story.MsgCommentEntity;
import io.renren.modules.app.entity.story.StoryTimelineEntity;
import io.renren.modules.app.entity.story.SubscribeEntity;
import io.renren.modules.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.app.dao.story.PublishMessageDao;
import io.renren.modules.app.entity.story.PublishMessageEntity;


@Service("publishMessageService")
public class PublishMessageServiceImpl extends ServiceImpl<PublishMessageDao, PublishMessageEntity> implements PublishMessageService {

    @Autowired
    private StoryTimelineService storyTimelineService;

    @Autowired
    private MsgCommentService msgCommentService;

    @Autowired
    private SubscribeService subscribeService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<PublishMessageEntity> page = this.selectPage(
                new Query<PublishMessageEntity>(params).getPage(),
                new EntityWrapper<PublishMessageEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void insertMsgAndTimeline(PublishMessageEntity publishMessageEntity) {
        insert(publishMessageEntity);
        //加入时间轴
        StoryTimelineEntity timeLine = new StoryTimelineEntity();
        timeLine.setPmId(publishMessageEntity.getId());
        timeLine.setIsOwn(1);
        timeLine.setMemberId(publishMessageEntity.getMemberId());
        timeLine.setCreateTime(System.currentTimeMillis());
        storyTimelineService.insert(timeLine);

        //查询当前用户的好友,并将msg存入好友的时间轴里
        List<SubscribeEntity> subList = subscribeService.selectList(new EntityWrapper<SubscribeEntity>().eq("member_id", publishMessageEntity.getMemberId()));
        for(SubscribeEntity sub : subList){
            StoryTimelineEntity ftimeline = new StoryTimelineEntity();
            ftimeline.setMemberId(sub.getSubscriberId());
            ftimeline.setIsOwn(0);
            ftimeline.setPmId(publishMessageEntity.getId());
            ftimeline.setCreateTime(System.currentTimeMillis());
            storyTimelineService.insert(ftimeline);
        }
    }

    @Override
    public void deleteMsgAndTimeline(Long id) {
        deleteById(id);
        //从时间轴上删除动态
        storyTimelineService.delete(new EntityWrapper<StoryTimelineEntity>().eq("pm_id",id));
        //删除动态的评论
        msgCommentService.delete(new EntityWrapper<MsgCommentEntity>().eq("pm_id",id));
    }

    @Override
    public List<PublishMessageEntity> getPage(HashMap<String, Object> param) {
        return baseMapper.getPage(param);
    }

    @Override
    public PublishMessageEntity getById(Long id) {
        return baseMapper.getById(id);
    }

}
