package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务
 */
@Mapper
public interface TaskDao extends BaseMapper<TaskEntity> {


    //获取任务详情
    TaskDto getTask(@Param("taskId") Long taskId);


    //分页获取用户的发布任务列表
    List<TaskDto> getPublishedTasks(@Param("publisherId") Long publisherId, @Param("page") PageWrapper page);
    //分页获取用户的领取任务列表
    List<TaskDto> getReceivedTasks(@Param("receiverId")Long receiverId, @Param("page")PageWrapper page);

    //插入任务-图片关系
    void insertTaskImageRelation(@Param("taskId") Long taskId, @Param("imageUrls") List<String> imageUrls);

    //插入任务-标签关系
    void insertTaskTagRelation(@Param("taskId") Long taskId, @Param("tagIds") List<Long> tagIds);

    //插入任务-被提示的用户关系
    void insertTaskNotifiedUserRelation(@Param("taskId") Long taskId, @Param("userIds") List<Long> userIds);

    /**
     * 任务发布总数
     * @param publisherId
     * @return
     */
    int publishCount(Long publisherId);
    /**
     * 任务领取总数
     * @param receiverId
     * @return
     */
    int receiveCount(Long receiverId);

}
