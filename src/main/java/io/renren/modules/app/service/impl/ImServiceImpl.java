package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.RedisKeys;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.dao.im.ImDao;
import io.renren.modules.app.dao.im.ImTaskStatusDao;
import io.renren.modules.app.dto.ImTaskStatusNoticeDto;
import io.renren.modules.app.dto.RedDotDto;
import io.renren.modules.app.entity.im.ImGroupNotice;
import io.renren.modules.app.entity.im.ImTaskStatusNotice;
import io.renren.modules.app.form.MessageTypeForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.ImService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ImServiceImpl extends ServiceImpl<ImDao, ImGroupNotice> implements ImService {
    private final static Logger LOG = LoggerFactory.getLogger(ImServiceImpl.class);
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private ImTaskStatusDao imTaskStatusDao;

    private static final long THIRTY_DAYS = 60 * 60 * 24 * 30;//30天

    @Override
    public PageUtils<ImGroupNotice> getGroupNotices(Long memberId, PageWrapper page) {
        List<ImGroupNotice> notices = baseMapper.getGroupNotices(memberId, page);
        if (CollectionUtils.isEmpty(notices)) {
            return new PageUtils<>();
        }
        int total = baseMapper.groupNoticeCount(memberId);
        return new PageUtils<>(notices, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void addGroupNotice(String groupId, String from, String businessType, Long businessId, String content) {
        ImGroupNotice notice = new ImGroupNotice();
        notice.setGroupId(groupId);
        notice.setFrom(from);
        notice.setBusinessId(businessId);
        notice.setBusinessType(businessType);
        notice.setContent(content);
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
    public void addTaskStatusNotice(String from, String to, String content, Long taskId) {
        ImTaskStatusNotice notice = new ImTaskStatusNotice();
        notice.setContent(content);
        notice.setFrom(from);
        notice.setTaskId(taskId);
        notice.setTo(to);
        notice.setCreateTime(DateUtils.now());
        imTaskStatusDao.insert(notice);
    }

    @Override
    public PageUtils<ImTaskStatusNoticeDto> getTaskStatusNotices(String to, PageWrapper page) {
        List<ImTaskStatusNoticeDto> notices = imTaskStatusDao.getTaskStatusNotices(to, page);
        if (CollectionUtils.isEmpty(notices)) {
            return new PageUtils<>();
        }
        int total = imTaskStatusDao.getTaskStatusNoticeCount(to);
        return new PageUtils<>(notices, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void setRedDot(Long memnberId, Integer redDotType) {
        switch (redDotType){
            case 0:
                redisUtils.set(RedisKeys.RED_DOT_CHAT+memnberId,Boolean.TRUE.toString(),THIRTY_DAYS);
                break;
            case 1:
                redisUtils.set(RedisKeys.RED_DOT_TASK+memnberId,Boolean.TRUE.toString(),THIRTY_DAYS);
                break;
            case 2:
                redisUtils.set(RedisKeys.RED_DOT_DYNAMIC+memnberId,Boolean.TRUE.toString(),THIRTY_DAYS);
                break;
        }
    }

    @Override
    public void cancelRedDot(Long memnberId, Integer redDotType) {
        switch (redDotType){
            case 0:
                redisUtils.delete(RedisKeys.RED_DOT_CHAT+memnberId);
                break;
            case 1:
                redisUtils.delete(RedisKeys.RED_DOT_TASK+memnberId);
                break;
            case 2:
                redisUtils.delete(RedisKeys.RED_DOT_DYNAMIC+memnberId);
                break;
        }
    }

    @Override
    public RedDotDto getRedDot(Long memberId) {
        RedDotDto redDot=new RedDotDto();
        redDot.setMemberId(memberId);
        String chatRedDotStatus = redisUtils.get(RedisKeys.RED_DOT_CHAT);
        String taskRedDotStatus = redisUtils.get(RedisKeys.RED_DOT_TASK);
        String dynamicRedDotStatus = redisUtils.get(RedisKeys.RED_DOT_DYNAMIC);
        if ("true".equals(chatRedDotStatus)){
            redDot.setChatRedDotStatus(true);
        }
        if ("true".equals(taskRedDotStatus)){
            redDot.setTaskRedDotStatus(true);
        }
        if ("true".equals(dynamicRedDotStatus)){
            redDot.setDynamicRedDotStatus(true);
        }
        return redDot;
    }

}