package io.renren.modules.app.dao.story;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.story.DiaryContentEntity;
import io.renren.modules.app.entity.story.DiaryEntity;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DiaryContentDao extends BaseMapper<DiaryContentEntity> {

}
