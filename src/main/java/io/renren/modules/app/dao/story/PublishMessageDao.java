package io.renren.modules.app.dao.story;

import io.renren.modules.app.entity.story.PublishMessageEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
@Mapper
public interface PublishMessageDao extends BaseMapper<PublishMessageEntity> {
	List<PublishMessageEntity> getPage(HashMap<String,Object> param);

	PublishMessageEntity getById(Long id);
}
