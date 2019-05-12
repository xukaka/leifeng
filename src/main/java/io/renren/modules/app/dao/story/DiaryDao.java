package io.renren.modules.app.dao.story;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangshishui
 * @date 2019/4/18 23:37
 **/
@Mapper
public interface DiaryDao extends BaseMapper<DiaryEntity> {

    //获取日记详情
    DiaryDto getDiary(@Param("id") Long id);

    //分页获取日记列表
    List<DiaryDto> getDiarys(@Param("page")PageWrapper page);

    //获取日记总数
    int count();

    //分页获取我的日记列表
    List<DiaryDto> getMyDiarys(@Param("creatorId")Long creatorId,@Param("page")PageWrapper page);

    //获取日记总数
    int myDiaryCount(@Param("creatorId")Long creatorId);

    //日记点赞数+inc
    void incLikeCount(@Param("diaryId")Long diaryId,@Param("inc") Integer inc);

    /**
     * 日记评论数 +inc
     */
    void incCommentCount(@Param("diaryId")Long diaryId,@Param("inc")Integer inc);

    /**
     * 日记浏览数 +inc
     */
    void incViewCount(@Param("diaryId")Long diaryId,@Param("inc")Integer inc);
}
