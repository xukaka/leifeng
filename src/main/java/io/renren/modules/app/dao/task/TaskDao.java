package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.dto.TaskBannerDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 任务
 */
@Mapper
public interface TaskDao extends BaseMapper<TaskEntity> {


    /**
     * 任务横幅列表-top15
     * @return
     */
    List<TaskBannerDto> getTaskBanners();

    //获取任务详情
    TaskDto getTask(@Param("taskId") Long taskId);

    /**
     * 分页搜索任务-根据查询条件
     *
     * @param queryMap
     * @param page
     * @return
     */
    List<TaskDto> searchTasks(@Param("queryMap") Map<String, Object> queryMap, @Param("page") PageWrapper page);

    //分页获取用户的发布任务列表
    List<TaskDto> getPublishedTasks(@Param("publisherId") Long publisherId, @Param("page") PageWrapper page);

    //分页获取用户的领取任务列表
    List<TaskDto> getReceivedTasks(@Param("receiverId") Long receiverId, @Param("page") PageWrapper page);

    //插入任务-图片关系
    void insertTaskImageRelation(@Param("taskId") Long taskId, @Param("imageUrls") List<String> imageUrls);

    //插入任务-标签关系
    void insertTaskTagRelation(@Param("taskId") Long taskId, @Param("tagIds") List<Long> tagIds);

    //插入任务-被提示的用户关系
//    void insertTaskNotifiedUserRelation(@Param("taskId") Long taskId, @Param("userIds") List<Long> userIds);

    /**
     * 任务发布总数
     *
     * @param publisherId
     * @return
     */
    int publishCount(@Param("publisherId") Long publisherId);

    /**
     * 任务领取总数
     *
     * @param receiverId
     * @return
     */
    int receiveCount(@Param("receiverId") Long receiverId);

    /**
     * 任务总数-根据查询条件
     *
     * @param queryMap
     * @return
     */
    int count(@Param("queryMap") Map<String, Object> queryMap);


    /**
     * 是否可领取任务
     * @param taskId
     * @return
     */
    int isReceiveableTask(@Param("receiverId")Long receiverId ,@Param("taskId")Long taskId );
    /**
     * 是否可提交任务
     * @param receiverId
     * @param taskId
     * @return
     */
    int isSubmitableTask(@Param("receiverId")Long receiverId,@Param("taskId")Long taskId );


    /**
     * 是否可完成任务
     */
    int isCompletableTask(@Param("receiverId")Long receiverId,@Param("taskId")Long taskId );

    /**
     * 分页获取任务领取人列表
     * @return
     */
    List<MemberDto> getTaskReceivers(@Param("taskId") Long taskId, @Param("page") PageWrapper page);

    /**
     * 任务领取人总数
     * @return
     */
    int receiverCount(@Param("taskId") Long taskId);

    /**
     * 是否可选择任务领取人
     * @param taskId
     * @return
     */
    int isChooseableReceiver(@Param("taskId")Long taskId ,@Param("receiverId")Long receiverId);

    /**
     * 任务是否可执行
     * @param taskId
     * @param receiverId
     * @return
     */
    int isExecutableTask(@Param("taskId")Long taskId ,@Param("receiverId")Long receiverId);
}
