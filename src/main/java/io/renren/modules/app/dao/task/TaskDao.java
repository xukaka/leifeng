package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务
 */
@Mapper
public interface TaskDao extends BaseMapper<TaskEntity> {


    //获取任务详情
    TaskDto getTask(@Param("taskId")Long taskId);

    //插入任务-图片关系
    void insertTaskImageRelation(@Param("taskId") Long taskId, @Param("imageUrls") List<String> imageUrls);

    //插入任务-标签关系
    void insertTaskTagRelation(@Param("taskId") Long taskId, @Param("tagIds") List<Long> tagIds);

    //插入任务-被提示的用户关系
    void insertTaskNotifiedUserRelation(@Param("taskId") Long taskId, @Param("userIds") List<Long> userIds);


}
