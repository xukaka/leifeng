package io.renren.modules.app.dao.story;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.entity.story.DiaryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangshishui
 * @date 2019/4/18 23:37
 **/
@Mapper
public interface DiaryDao extends BaseMapper<DiaryEntity> {

    //获取日记详情
    DiaryDto getDiary(@Param("id") Long id);
}
