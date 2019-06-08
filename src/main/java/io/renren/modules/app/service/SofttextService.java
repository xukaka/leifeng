package io.renren.modules.app.service;

import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.BannerDto;
import io.renren.modules.app.dto.SofttextDto;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.form.*;

/**
 * 软文
 */
public interface SofttextService {

    /**
     * 获取软文列表
     */
    PageUtils<SofttextDto> getSofttexts(PageWrapper page);


    //创建软文
    void createSofttext(SofttextForm form);
    //获取软文详情
    SofttextDto getSofttext(Long id);

    //修改软文信息
    void updateSofttext(SofttextUpdateForm form);

    //删除软文
    void deleteSofttext(Long id);
}

