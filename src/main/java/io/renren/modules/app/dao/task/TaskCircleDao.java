package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskCircleEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务圈
 */
@Mapper
public interface TaskCircleDao extends BaseMapper<TaskCircleEntity> {


    //获取任务圈详情
    TaskCircleDto getCircle(@Param("circleId") Long circleId);


}
