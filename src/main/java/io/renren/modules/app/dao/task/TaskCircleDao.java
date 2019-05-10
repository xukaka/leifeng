package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.common.utils.PageUtils;
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


    /**
     * 分页获取任务圈列表
     * @param circleName
     * @param page
     * @return
     */
    List<TaskCircleDto> getCircles(@Param("keyword") String circleName, @Param("page") PageWrapper page);


    /**
     * 分页获取我加入的任务圈列表
     * @param memberId
     * @param page
     * @return
     */
    List<TaskCircleDto> getMyJoinedCircles(Long memberId, PageWrapper page);

    //任务圈总数
    int count(@Param("keyword") String circleName, @Param("page") PageWrapper page);

    //任务圈总数-我加入的
    int myJoinedCount(@Param("memberId") Long memberId, @Param("page") PageWrapper page);


    //圈成员+inc
    void incCircleMemberCount(@Param("circleId")Long circleId,@Param("inc")Integer inc);

    //插入圈-标签关系
    void insertCircleTagRelation(@Param("circleId") Long circleId, @Param("tagIds") List<Long> tagIds);
      //删除圈-标签关系
    void deleteCircleTagRelation(@Param("circleId") Long circleId);
}
