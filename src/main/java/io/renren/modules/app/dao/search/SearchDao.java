package io.renren.modules.app.dao.search;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.HotSearchDto;
import io.renren.modules.app.entity.search.SearchHistoryEntity;
import io.renren.modules.app.entity.search.SearchLogEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 搜索
 */
@Mapper
public interface SearchDao extends BaseMapper<SearchHistoryEntity> {


    /**
     * 插入搜索日志
     */
    void insertLog(SearchLogEntity log);
    /**
     * 统计热门搜索
     * @return
     */
    List<HotSearchDto> getHotSearch();
}
