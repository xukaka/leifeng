package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.CommentDto;
import io.renren.modules.app.entity.CommentTypeEnum;
import io.renren.modules.app.entity.task.CommentEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务评论
 */
@Mapper
public interface CommentDao extends BaseMapper<CommentEntity> {

    /**
     * 分页获取任务评论
     */
    List<CommentDto> getComments(@Param("businessId") Long businessId, @Param("type")CommentTypeEnum type,@Param("page") PageWrapper page);

    /**
     * 评论总数
     */
    int count(@Param("businessId") Long businessId,@Param("type") CommentTypeEnum type);


}
