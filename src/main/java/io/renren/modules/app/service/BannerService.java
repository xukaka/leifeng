package io.renren.modules.app.service;

import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.BannerDto;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.form.BannerForm;
import io.renren.modules.app.form.BannerUpdateForm;
import io.renren.modules.app.form.PageWrapper;

/**
 * banner
 */
public interface BannerService {

    /**
     * 获取横幅列表
     */
    PageUtils<BannerDto> getBanners(BannerTypeEnum type, PageWrapper page);


    //创建横幅
    void createBanner(BannerForm form);
    //获取横幅详情
    BannerDto getBanner(Long id);

    //修改横幅信息
    void updateBanner(BannerUpdateForm form);

    //删除横幅
    void deleteBanner(Long id);
}

