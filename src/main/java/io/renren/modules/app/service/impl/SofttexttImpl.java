package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.banner.SofttextDao;
import io.renren.modules.app.dto.BannerDto;
import io.renren.modules.app.dto.SofttextDto;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.entity.banner.BannerEntity;
import io.renren.modules.app.entity.banner.SofttextEntity;
import io.renren.modules.app.form.*;
import io.renren.modules.app.service.SofttextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Service
public class SofttexttImpl extends ServiceImpl<SofttextDao, SofttextEntity> implements SofttextService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public PageUtils<SofttextDto> getSofttexts(PageWrapper page) {
        List<SofttextDto> softtexts = baseMapper.getSofttexts( page);
        if (CollectionUtils.isEmpty(softtexts)) {
            return new PageUtils<>();
        }
        for (SofttextDto softtext:softtexts){
            softtext.setLinkUrl(softtext.getLinkUrl()+softtext.getId());
        }

        int total = baseMapper.count();
        return new PageUtils<>(softtexts, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public void createSofttext( SofttextForm form) {

        ValidatorUtils.validateEntity(form);
        logger.info("createSofttext method:from={}",form);
        SofttextEntity softtext = new SofttextEntity();
        BeanUtils.copyProperties(form, softtext);
        softtext.setCreateTime(DateUtils.now());
        this.insert(softtext);

    }

    @Override
    public SofttextDto getSofttext(Long id) {
       return baseMapper.getSofttext(id);
    }

    @Override
    public void updateSofttext(SofttextUpdateForm form) {
        ValidatorUtils.validateEntity(form);
        SofttextEntity softtext = this.selectById(form.getId());
        softtext.setTitle(form.getTitle());
        softtext.setLinkUrl(form.getLinkUrl());
        softtext.setHtmlContent(form.getHtmlContent());
        softtext.setSource(form.getSource());
        this.updateById(softtext);
    }

    @Override
    public void deleteSofttext(Long id) {
        SofttextEntity softtext = this.selectById(id);
        softtext.setDeleted(true);
        this.updateById(softtext);
    }
}
