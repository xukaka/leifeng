package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.app.dto.HotSearchDto;
import io.renren.modules.app.entity.search.SearchHistoryEntity;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.form.TaskAddressForm;

import java.util.List;

/**
 * 搜索
 */
public interface SearchService extends IService<SearchHistoryEntity> {
    /**
     * 保存历史
     */
    void saveHistory(Long userId, String keyword);

    /**
     * 获取用户的搜索历史列表
     */
    List<SearchHistoryEntity> getHistories(Long userId);

    /**
     * 根据ids清空用户的搜索历史-逻辑删除
     */
    void clearHistories(Long[] historyIds);


    /**
     * 获取热门搜索
     * 最近一个月，搜索次数最多的top10关键字
     */
    List<HotSearchDto> getHotSearch();

}

