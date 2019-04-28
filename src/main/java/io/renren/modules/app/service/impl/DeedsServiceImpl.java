package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.app.dao.story.DeedsDao;
import io.renren.modules.app.dto.DeedsDto;
import io.renren.modules.app.entity.story.DeedsEntity;
import io.renren.modules.app.form.DeedsForm;
import io.renren.modules.app.service.DeedsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author huangshishui
 * @date 2019/4/18 23:59
 **/
@Service("deedsService")
public class DeedsServiceImpl extends ServiceImpl<DeedsDao,DeedsEntity> implements DeedsService {
    private final static Logger logger = LoggerFactory.getLogger(DeedsServiceImpl.class);

    @Autowired
    private DeedsDao deedsDao;

    @Override
    public void insertDeedsInfo(DeedsForm deedForm) throws Exception {
        String deedsType = deedForm.getDeedsType();
        int ranNum = (int)(Math.random()*9 + 1)*100;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
        String subTime = sdf.format(new Date());
        String deedsId = deedsType+ranNum+subTime;
        logger.info("【生成deedId】："+deedsId);
        DeedsEntity deedsEntity = new DeedsEntity();
        deedsEntity.setUserId(deedForm.getUserId());
        deedsEntity.setDeedsId(deedsId);
        //deedsEntity.setId(Long.parseLong(subTime));
        deedsEntity.setDeedsType(deedForm.getDeedsType());
        deedsEntity.setReadCount(0);
        deedsEntity.setContext(deedForm.getContext());
        deedsEntity.setTitle(deedForm.getTitle());
        deedsEntity.setImageUrl(deedForm.getImageUrl());
        deedsEntity.setIsOpen(deedForm.getIsOpen());

        deedsEntity.setCreatedBy(deedForm.getUserId());
        deedsEntity.setCreatedTime(new Date());
        deedsEntity.setUpdatedBy(deedForm.getUserId());
        deedsEntity.setUpdatedTime(new Date());
        this.insert(deedsEntity);
    }

    @Override
    public void updateReadCount(String deedsId) {
        DeedsEntity deedsEntity = baseMapper.queryDeedsEntityById(deedsId);
        int count = deedsEntity.getReadCount()+1;
        deedsEntity.setReadCount(count);
        this.updateById(deedsEntity);
    }

    @Override
    public List<DeedsDto> queryDeedsList(String userId) throws Exception {
        /*Wrapper<DeedsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId).orderBy("created_time");
        List<DeedsEntity> deedsList = this.selectList(wrapper);
        if(CollectionUtils.isEmpty(deedsList)){
            return new ArrayList<>();
        }*/
        List<DeedsDto> deedsList = baseMapper.queryDeedsEntityByUserId(userId);
        return deedsList;
    }
}
