package io.renren.modules.app.service.impl;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.RedisKeys;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.dao.diary.DiaryDao;
import io.renren.modules.app.dao.im.ImCircleDao;
import io.renren.modules.app.dao.im.ImDynamicDao;
import io.renren.modules.app.dao.im.ImTaskDao;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.dto.*;
import io.renren.modules.app.entity.im.ImCircleNotice;
import io.renren.modules.app.entity.im.ImDynamicNotice;
import io.renren.modules.app.entity.im.ImHistoryMember;
import io.renren.modules.app.entity.im.ImTaskNotice;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.MessageTypeForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.ImService;
import org.jim.server.helper.redis.RedisMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImServiceImpl implements ImService {
    private final static Logger LOG = LoggerFactory.getLogger(ImServiceImpl.class);
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private ImTaskDao imTaskDao;

    @Resource
    private ImCircleDao imCircleDao;
    @Resource
    private ImDynamicDao imDynamicDao;
    @Resource
    private TaskDao taskDao;
    @Resource
    private DiaryDao diaryDao;

    private RedisMessageHelper redisMessageHelper = new RedisMessageHelper();


    private static final long THIRTY_DAYS = 60 * 60 * 24 * 30;//30天

    @Override
    public PageUtils<ImDynamicNoticeDto> getDynamicNotices(Long memberId, PageWrapper page) {
        List<ImDynamicNoticeDto> notices = imDynamicDao.getDynamicNotices(memberId, page);
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
        int total = imDynamicDao.getDynamicNoticeCount(memberId);
        return new PageUtils<>(notices, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void addDynamicNotice(Long memberId, String businessType, Long businessId){
        ImDynamicNotice notice = new ImDynamicNotice();
        notice.setMemberId(memberId);
        notice.setBusinessId(businessId);
        notice.setBusinessType(businessType);
        notice.setCreateTime(DateUtils.now());
        imDynamicDao.insert(notice);
    }


    @Override
    public void setMessageType(Long fromId, Long toId) {
        LOG.info("setMessageType method:fromId={},toId={}", fromId, toId);

        redisUtils.zAdd("unread:" + fromId, toId, 0);
        if (isOnline(toId)) {
            redisUtils.zAdd("unread:" + toId, fromId, 0);//已读
        } else {
            redisUtils.zAdd("unread:" + toId, fromId, 1);//未读
        }
    }

    //用户是否在线
   private boolean isOnline(Long userId) {
      return redisMessageHelper.isOnline(String.valueOf(userId));
  /*      String onlineStatus = redisUtils.get("user:" + String.valueOf(userId) + ":terminal:ws");//目标用户是否在线
        return "online".equals(onlineStatus);*/
    }

    @Override
    public void addTaskNotice(Long fromMemberId, Long toMemberId, String operate, Long taskId) {
        ImTaskNotice notice = new ImTaskNotice();
        notice.setOperate(operate);//操作
        notice.setFromMemberId(fromMemberId);//发送方id
        notice.setToMemberId(toMemberId);//接收方id
        notice.setTaskId(taskId);
        notice.setCreateTime(DateUtils.now());
        imTaskDao.insert(notice);
    }

    @Override
    public PageUtils<ImTaskNoticeDto> getTaskNotices(Long memberId, PageWrapper page) {
        List<ImTaskNoticeDto> notices = imTaskDao.getTaskNotices(memberId, page);
        if (CollectionUtils.isEmpty(notices)) {
            return new PageUtils<>();
        }
        int total = imTaskDao.getTaskNoticeCount(memberId);
        return new PageUtils<>(notices, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void addCircleNotice(Long circleId, Long auditId, Long fromMemberId, Long toMemberId, String type) {
        ImCircleNotice notice = new ImCircleNotice();
        notice.setCircleId(circleId);
        notice.setAuditId(auditId);
        notice.setFromMemberId(fromMemberId);
        notice.setToMemberId(toMemberId);
        notice.setType(type);
        notice.setCreateTime(DateUtils.now());
        imCircleDao.insert(notice);
    }

    @Override
    public PageUtils<ImCircleNoticeDto> getCircleNotices(Long toMemberId, PageWrapper page) {
        List<ImCircleNoticeDto> notices = imCircleDao.getCircleNotices(toMemberId, page);
        if (CollectionUtils.isEmpty(notices)) {
            return new PageUtils<>();
        }
        int total = imCircleDao.getCircleNoticeCount(toMemberId);
        return new PageUtils<>(notices, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void setRedDot(Long memberId, Integer redDotType) {
        switch (redDotType) {
            case 1:
                redisUtils.set(RedisKeys.RED_DOT_TASK + memberId, Boolean.TRUE.toString(), THIRTY_DAYS);
                break;
            case 2:
                redisUtils.set(RedisKeys.RED_DOT_DYNAMIC + memberId, Boolean.TRUE.toString(), THIRTY_DAYS);
                break;
            case 3:
                redisUtils.set(RedisKeys.RED_DOT_CIRCLE + memberId, Boolean.TRUE.toString(), THIRTY_DAYS);
                break;
        }
    }

    @Override
    public void cancelRedDot(Long memberId, Integer redDotType, Long toId) {
        switch (redDotType) {
            case 0:
                if (toId != null) {
                    redisUtils.zAdd("unread:" + memberId, toId, 0);//设置已读
                }
                break;
            case 1:
                redisUtils.delete(RedisKeys.RED_DOT_TASK + memberId);
                break;
            case 2:
                redisUtils.delete(RedisKeys.RED_DOT_DYNAMIC + memberId);
                break;
            case 3:
                redisUtils.delete(RedisKeys.RED_DOT_CIRCLE + memberId);
                break;
        }
    }

    @Override
    public RedDotDto getRedDot(Long memberId) {
        RedDotDto redDot = new RedDotDto();
        String taskRedDotStatus = redisUtils.get(RedisKeys.RED_DOT_TASK + memberId);
        String dynamicRedDotStatus = redisUtils.get(RedisKeys.RED_DOT_DYNAMIC + memberId);
        String circleRedDotStatus = redisUtils.get(RedisKeys.RED_DOT_CIRCLE + memberId);
        List<ChatRedDot> chatRedDots = getChatRedDots(memberId);
        redDot.setChatRedDotList(chatRedDots);

        if (Boolean.valueOf(taskRedDotStatus)) {
            redDot.setTaskRedDotStatus(true);
        }
        if (Boolean.valueOf(dynamicRedDotStatus)) {
            redDot.setDynamicRedDotStatus(true);
        }
        if (Boolean.valueOf(circleRedDotStatus)) {
            redDot.setCircleRedDotStatus(true);
        }
        return redDot;
    }

    //获取用户的红点状态列表
    private List<ChatRedDot> getChatRedDots(Long memberId) {
        List<ChatRedDot> chatRedDots = new ArrayList<>();
        List<Map<String, Object>> list = redisUtils.rangeByScore("unread:" + memberId, ImHistoryMember.class);
        if (!CollectionUtils.isEmpty(list)) {
            for (Map<String, Object> map : list) {
                ChatRedDot chatRedDot = new ChatRedDot();
                Double score = (Double) map.get("score");
                if (score.intValue() == 1) {//未读
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