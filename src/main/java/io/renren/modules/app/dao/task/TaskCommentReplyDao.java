package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.TaskCommentDto;
import io.renren.modules.app.dto.TaskCommentReplyDto;
import io.renren.modules.app.entity.task.TaskCommentEntity;
import io.renren.modules.app.entity.task.TaskCommentReplyEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务评论回复
 */
@Mapper
public interface TaskCommentReplyDao extends BaseMapper<TaskCommentReplyEntity> {

    /**
     * 获取任务评论回复列表
     */
    List<TaskCommentReplyDto> getCommentReplies(@Param("commentId") Long commentId);

}
