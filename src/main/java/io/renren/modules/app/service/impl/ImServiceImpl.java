package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.*;
import io.renren.modules.app.dao.diary.DiaryDao;
import io.renren.modules.app.dao.im.ImDynamicDao;
import io.renren.modules.app.dao.im.ImTaskDao;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.dto.ChatRedDot;
import io.renren.modules.app.dto.ImDynamicNoticeDto;
import io.renren.modules.app.dto.ImTaskNoticeDto;
import io.renren.modules.app.dto.RedDotDto;
import io.renren.modules.app.entity.im.ImDynamicNotice;
import io.renren.modules.app.entity.im.ImHistoryMember;
import io.renren.modules.app.entity.im.ImTaskNotice;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.MessageTypeForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.ImService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImServiceImpl extends ServiceImpl<ImDynamicDao, ImDynamicNotice> implements ImService {
    private final static Logger LOG = LoggerFactory.getLogger(ImServiceImpl.class);
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private ImTaskDao imTaskStatusDao;
    @Resource
    private TaskDao taskDao;
    @Resource
    private DiaryDao diaryDao;


    private static final long THIRTY_DAYS = 60 * 60 * 24 * 30;//30天

    @Override
    public PageUtils<ImDynamicNoticeDto> getDynamicNotices(Long memberId, PageWrapper page) {
        List<ImDynamicNoticeDto> notices = baseMapper.getDynamicNotices(memberId, page);
        if (CollectionUtils.isEmpty(notices)) {
            return new PageUtils<>();
        }
        for (ImDynamicNoticeDto notice : notices) {
            switch (notice.getBusinessType()) {
                case "task":
                    TaskEntity task = taskDao.selectById(notice.getBusinessId());
                    if (task != null) {
                        notice.setBusinessTitle(task.getTitle());
                    }
                    break;
                case "diary":
                    DiaryEntity diary = diaryDao.selectById(notice.getBusinessId());
                    if (diary != null) {
                        notice.setBusinessTitle(diary.getTitle());
                    }
                    break;
            }
        }
        int total = baseMapper.getDynamicNoticeCount(memberId);
        return new PageUtils<>(notices, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void addDynamicNotice(Long memberId, String businessType, Long businessId) {
        ImDynamicNotice notice = new ImDynamicNotice();
        notice.setMemberId(memberId);
        notice.setBusinessId(businessId);
        notice.setBusinessType(businessType);
        notice.setCreateTime(DateUtils.now());
        this.insert(notice);
    }


    @Override
    public void setMessageType(MessageTypeForm messageTypeForm) {
        LOG.info("[Im.setMessageType] 请求参数messageTypeForm={}", messageTypeForm);
        if (messageTypeForm.getStatus() == 1 && messageTypeForm.getType() == 0) {
            redisUtils.zAdd("unread:" + messageTypeForm.getToId(), messageTypeForm.getFromId(), messageTypeForm.getStatus());
        } else {
            redisUtils.zAdd("unread:" + messageTypeForm.getToId(), messageTypeForm.getFromId(), messageTypeForm.getStatus());
            redisUtils.zAdd("unread:" + messageTypeForm.getFromId(), messageTypeForm.getToId(), messageTypeForm.getStatus());
        }
        if (messageTypeForm.getType() == 1) {
            redisUtils.zAdd("followNotice:" + messageTypeForm.getToId(), "followNotice", messageTypeForm.getStatus());
        }
        if (messageTypeForm.getType() == 2) {
            redisUtils.zAdd("task:" + messageTypeForm.getToId(), "SystemTaskStatus", messageTypeForm.getStatus());
        }
    }

    @Override
    public void addTaskNotice(Long memberId, String operate, Long taskId) {
        ImTaskNotice notice = new ImTaskNotice();
        notice.setOperate(operate);//操作
        notice.setMemberId(memberId);//接收方id
        notice.setTaskId(taskId);
        notice.setCreateTime(DateUtils.now());
        imTaskStatusDao.insert(notice);
    }

    @Override
    public PageUtils<ImTaskNoticeDto> getTaskNotices(Long memberId, PageWrapper page) {
        List<ImTaskNoticeDto> notices = imTaskStatusDao.getTaskNotices(memberId, page);
        if (CollectionUtils.isEmpty(notices)) {
            return new PageUtils<>();
        }
        int total = imTaskStatusDao.getTaskNoticeCount(memberId);
        return new PageUtils<>(notices, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void setRedDot(Long memberId, Integer redDotType) {
        switch (redDotType) {
           /* case 0:
                redisUtils.set(RedisKeys.RED_DOT_CHAT + memnberId, Boolean.TRUE.toString(), THIRTY_DAYS);
                break;*/
            case 1:
                redisUtils.set(RedisKeys.RED_DOT_TASK + memberId, Boolean.TRUE.toString(), THIRTY_DAYS);
                break;
            case 2:
                redisUtils.set(RedisKeys.RED_DOT_DYNAMIC + memberId, Boolean.TRUE.toString(), THIRTY_DAYS);
                break;
        }
    }

    @Override
    public void cancelRedDot(Long memberId, Integer redDotType,Long toId) {
        switch (redDotType) {
            case 0:
//                redisUtils.delete(RedisKeys.RED_DOT_CHAT + memnberId);
                if (toId!=null){
                    redisUtils.zAdd("unread:" + memberId, toId,1);//设置已读
                }
                break;
            case 1:
                redisUtils.delete(RedisKeys.RED_DOT_TASK + memberId);
                break;
            case 2:
                redisUtils.delete(RedisKeys.RED_DOT_DYNAMIC + memberId);
                break;
        }
    }

    @Override
    public RedDotDto getRedDot(Long memberId) {

        RedDotDto redDot = new RedDotDto();
//        redDot.setMemberId(memberId);
//        String chatRedDotStatus = redisUtils.get(RedisKeys.RED_DOT_CHAT);
        String taskRedDotStatus = redisUtils.get(RedisKeys.RED_DOT_TASK+memberId);
        String dynamicRedDotStatus = redisUtils.get(RedisKeys.RED_DOT_DYNAMIC+memberId);
        List<ChatRedDot> chatRedDots = getChatRedDots(memberId);
        redDot.setChatRedDotList(chatRedDots);
       /* if ("true".equals(chatRedDotStatus)) {
            redDot.setChatRedDotStatus(true);
        }*/
        if ("true".equals(taskRedDotStatus)) {
            redDot.setTaskRedDotStatus(true);
        }
        if ("true".equals(dynamicRedDotStatus)) {
            redDot.setDynamicRedDotStatus(true);
        }
        return redDot;
    }

    //获取用户的红点状态列表
    private List<ChatRedDot>  getChatRedDots(Long memberId) {
        List<ChatRedDot> chatRedDots = new ArrayList<>();
        List<Map<String, Object>> list = redisUtils.rangeByScore("unread:" + memberId, ImHistoryMember.class);
        if (!CollectionUtils.isEmpty(list)) {
            for (Map<String, Object> map : list) {
                ChatRedDot chatRedDot = new ChatRedDot();
                Double score = (Double) map.get("score");
                if (score.intValue()==0){//未读
                    chatRedDot.setStatus(true);
                }

                if (map.get("value") != null) {
                    chatRedDot.setMemberId(Long.parseLong(map.get("value").toString()));
                }
                chatRedDots.add(chatRedDot);
            }
        }
        return chatRedDots;
    }

}