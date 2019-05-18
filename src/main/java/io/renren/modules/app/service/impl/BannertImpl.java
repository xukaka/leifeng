package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.banner.BannerDao;
import io.renren.modules.app.dto.BannerDto;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.entity.banner.BannerEntity;
import io.renren.modules.app.form.BannerForm;
import io.renren.modules.app.form.BannerUpdateForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.BannerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Service
public class BannertImpl extends ServiceImpl<BannerDao, BannerEntity> implements BannerService {


    @Override
    public PageUtils<BannerDto> getBanners(BannerTypeEnum type, PageWrapper page) {
        List<BannerDto> banners = baseMapper.getBanners(type, page);
        if (CollectionUtils.isEmpty(banners)) {
            return new PageUtils<>();
        }
        int total = baseMapper.count(type);
        return new PageUtils<>(banners, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void createBanner(Long creatorId, BannerForm form) {
        ValidatorUtils.validateEntity(form);
        BannerEntity banner = new BannerEntity();
        BeanUtils.copyProperties(form, banner);
        banner.setCreatorId(creatorId);
        banner.setCreateTime(DateUtils.now());
        this.insert(banner);

    }

    @Override
    public BannerDto getBanner(Long bannerId) {
       return baseMapper.getBanner(bannerId);
    }

    @Override
    public void updateBanner(BannerUpdateForm form) {
        ValidatorUtils.validateEntity(form);
        BannerEntity banner = this.selectById(form.getId());
        banner.setCoverUrl(form.getCoverUrl());
        banner.setLinkUrl(form.getLinkUrl());
        banner.setTitle(form.getTitle());
        banner.setType(form.getType());
        this.updateById(banner);
    }

    @Override
    public void deleteBanner(Long id) {
        BannerEntity banner = this.selectById(id);
        banner.setDeleted(true);
        this.updateById(banner);
    }
}
