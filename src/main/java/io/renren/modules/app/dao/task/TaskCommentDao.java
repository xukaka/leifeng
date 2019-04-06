package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.TaskCommentDto;
import io.renren.modules.app.entity.task.TaskCommentEntity;
import io.renren.modules.app.form.PageForm;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务评论
 */
@Mapper
public interface TaskCommentDao extends BaseMapper<TaskCommentEntity> {

    /**
     * 分页获取任务评论
     */
    List<TaskCommentDto> getComments(@Param("taskId") Long taskId,@Param("page") PageWrapper page);

    /**
     * 评论总数
     * @param taskId
     * @return
     */
    int count(@Param("taskId") Long taskId);


}
