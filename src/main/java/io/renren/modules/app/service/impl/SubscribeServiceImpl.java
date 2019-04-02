package io.renren.modules.app.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.app.dao.story.SubscribeDao;
import io.renren.modules.app.entity.story.SubscribeEntity;
import io.renren.modules.app.service.SubscribeService;


@Service("subscribeService")
public class SubscribeServiceImpl extends ServiceImpl<SubscribeDao, SubscribeEntity> implements SubscribeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SubscribeEntity> page = this.selectPage(
                new Query<SubscribeEntity>(params).getPage(),
                new EntityWrapper<SubscribeEntity>()
        );

        return new PageUtils(page);
    }

}
