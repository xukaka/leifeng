package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.CommentReplyDto;
import io.renren.modules.app.entity.task.CommentReplyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务评论回复
 */
@Mapper
public interface CommentReplyDao extends BaseMapper<CommentReplyEntity> {

    /**
     * 获取任务评论回复列表
     */
    List<CommentReplyDto> getCommentReplies(@Param("commentId") Long commentId);

}
