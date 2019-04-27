package io.renren.modules.app.dao.story;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.DeedsDto;
import io.renren.modules.app.entity.story.DeedsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author huangshishui
 * @date 2019/4/18 23:37
 **/
@Mapper
public interface DeedsDao extends BaseMapper<DeedsEntity> {

    DeedsEntity queryDeedsEntityById(String deedsId);

    List<DeedsDto> queryDeedsEntityByUserId(String userId);
}
