package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.task.TaskTagEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务标签
 */
@Mapper
public interface TaskTagDao extends BaseMapper<TaskTagEntity> {
    /**
     * 标签是否存在
     */
//    boolean exists(String name);
}
