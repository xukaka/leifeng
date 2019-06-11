package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.entity.task.TaskCircleAuditEntity;
import io.renren.modules.app.entity.task.TaskCircleEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 圈审核
 */
@Mapper
public interface TaskCircleAuditDao extends BaseMapper<TaskCircleAuditEntity> {

}
