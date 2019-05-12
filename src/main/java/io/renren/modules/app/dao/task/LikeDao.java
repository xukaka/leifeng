package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.task.LikeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 点赞
 */
@Mapper
public interface LikeDao extends BaseMapper<LikeEntity> {

}
