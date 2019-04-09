package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskCircleEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskCircleForm;
import io.renren.modules.app.form.TaskForm;

import java.util.Map;

/**
 * 任务圈
 */
public interface TaskCircleService extends IService<TaskCircleEntity> {

    /**
     * 创建任务圈
     */
    void createCircle(Long creatorId, TaskCircleForm form);

    /**
     * 获取任务圈-根据id
     */
    TaskCircleDto getCircle(Long circleId);

    /**
     * 更新任务圈信息
     * @param form
     */
    void updateCircle(TaskCircleForm form);

    /**
     * 解散任务圈
     * @param circleId
     */
    void dismissCircle(Long circleId);


    /**
     * 分页获取任务圈列表
     * @return
     */
    PageUtils<TaskCircleDto> getCircles(String circleName, PageWrapper page);


    /**
     * 加入任务圈
     * @param currentUserId
     * @param circleId
     */
    void joinCircle(Long currentUserId, Long circleId);

    /**
     * 退出任务圈
     * @param currentUserId
     * @param circleId
     */
    void exitCircle(Long currentUserId, Long circleId);
}

