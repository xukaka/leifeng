package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.RedisKeys;
import io.renren.common.utils.RedisUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.banner.BannerDao;
import io.renren.modules.app.dto.BannerDto;
import io.renren.modules.app.dto.TaskBannerDto;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.entity.banner.BannerEntity;
import io.renren.modules.app.form.BannerForm;
import io.renren.modules.app.form.BannerUpdateForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.BannerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;


@Service
public class BannertImpl extends ServiceImpl<BannerDao, BannerEntity> implements BannerService {

    private static final int BANNERS_EXPIRE = 2 * 60 * 60;//2小时;

    private static final Type gsonType = new TypeToken<PageUtils<BannerDto>>() {
    }.getType();

    @Resource
    private RedisUtils redisUtils;

    @Override
    public PageUtils<BannerDto> getBanners(BannerTypeEnum type, PageWrapper page) {
        String redisKey = RedisKeys.BANNER_TYPE_KEY + type.name();
        String json = redisUtils.get(redisKey);
        if (!StringUtils.isEmpty(json)) {
            return new Gson().fromJson(json, gsonType);
        } else {
            List<BannerDto> banners = baseMapper.getBanners(type, page);
            if (CollectionUtils.isEmpty(banners)) {
                return new PageUtils<>();
            }
            int total = baseMapper.count(type);
            PageUtils<BannerDto> result = new PageUtils<>(banners, total, page.getPageSize(), page.getCurrPage());
            redisUtils.set(redisKey, result, BANNERS_EXPIRE);
            return result;
        }
    }

    @Override
    public void createBanner(BannerForm form) {
        ValidatorUtils.validateEntity(form);
        BannerEntity banner = new BannerEntity();
        BeanUtils.copyProperties(form, banner);
        banner.setCreateTime(DateUtils.now());
        this.insert(banner);

    }

    @Override
    public BannerDto getBanner(Long id) {
        return baseMapper.getBanner(id);
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
