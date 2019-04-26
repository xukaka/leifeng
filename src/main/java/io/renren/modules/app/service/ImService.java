package io.renren.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.im.ImGroupNotice;
import io.renren.modules.app.form.PageWrapper;

/**
 * IM
 */
public interface ImService extends IService<ImGroupNotice> {
    /**
     * 分页获取群组通知列表
     *
     */
    PageUtils<ImGroupNotice> getGroupNotices(Long memberId, PageWrapper page);

    /**
     * 添加群组通知
     */
    void addGroupNotice(Long groupId, String extrasJson);

}
