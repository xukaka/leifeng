package io.renren.modules.app.dao.member;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.MemberScoreDto;
import io.renren.modules.app.dto.ScoreBoardDto;
import io.renren.modules.app.entity.member.MemberScoreEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户评分表
 */
@Mapper
public interface MemberScoreDao extends BaseMapper<MemberScoreEntity> {
    //获取评分面板数据
    ScoreBoardDto getScoreBoard(@Param("memberId") Long memberId);

    //获取评分详情-根据任务id
    MemberScoreDto getScore(Long taskId);

    //分页获取用户评分列表
    List<MemberScoreDto> getMemberScores(@Param("memberId") Long memberId, @Param("page") PageWrapper page);

    //用户评分总数
    int count(@Param("memberId") Long memberId);


}
