package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.story.PublishMessageEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
public interface PublishMessageService extends IService<PublishMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void insertMsgAndTimeline(PublishMessageEntity publishMessageEntity);

    void deleteMsgAndTimeline(Long id);

    List<PublishMessageEntity> getPage(HashMap<String,Object> param);

    PublishMessageEntity getById(Long id);
}

