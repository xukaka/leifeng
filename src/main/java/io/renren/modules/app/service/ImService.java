package io.renren.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.ImDynamicNoticeDto;
import io.renren.modules.app.dto.ImTaskStatusNoticeDto;
import io.renren.modules.app.dto.RedDotDto;
import io.renren.modules.app.entity.im.ImDynamicNotice;
import io.renren.modules.app.form.MessageTypeForm;
import io.renren.modules.app.form.PageWrapper;

/**
 * IM
 */
public interface ImService extends IService<ImDynamicNotice> {
    /**
     * 分页获取最新动态通知列表
     */
    PageUtils<ImDynamicNoticeDto> getDynamicNotices(Long memberId, PageWrapper page);

    /**
     * 添加最新动态通知
     */
    void addDynamicNotice(Long memberId, String businessType, Long businessId);

    /**
     * 设置消息类型
     */
    void setMessageType(MessageTypeForm messageTypeForm);


    //添加任务状态通知
    void addTaskStatusNotice(String from, String to, String content, Long taskId);

    //分页获取任务状态通知列表
    PageUtils<ImTaskStatusNoticeDto> getTaskStatusNotices(String to, PageWrapper page);


    /**
     * 设置红点
     *
     * redDotType:0聊天，1任务，2动态
     */
    void setRedDot(Long memnberId,Integer redDotType);
    /**
     * 取消红点
     * redDotType:0聊天，1任务，2动态
     */
    void cancelRedDot(Long memnberId,Integer redDotType);

    /**
     * 获取红点
     * @param memberId
     * @return
     */
    RedDotDto getRedDot(Long memberId);






}
