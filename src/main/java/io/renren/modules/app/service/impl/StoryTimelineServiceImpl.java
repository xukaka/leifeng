package io.renren.modules.app.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.app.dao.story.StoryTimelineDao;
import io.renren.modules.app.entity.story.StoryTimelineEntity;
import io.renren.modules.app.service.StoryTimelineService;


@Service("storyTimelineService")
public class StoryTimelineServiceImpl extends ServiceImpl<StoryTimelineDao, StoryTimelineEntity> implements StoryTimelineService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<StoryTimelineEntity> page = this.selectPage(
                new Query<StoryTimelineEntity>(params).getPage(),
                new EntityWrapper<StoryTimelineEntity>()
        );

        return new PageUtils(page);
    }

}
