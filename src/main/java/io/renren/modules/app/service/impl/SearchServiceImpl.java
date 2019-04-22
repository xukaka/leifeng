package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.RedisKeys;
import io.renren.common.utils.RedisUtils;
import io.renren.config.SwaggerConfig;
import io.renren.modules.app.dao.search.SearchDao;
import io.renren.modules.app.dao.task.TaskTagDao;
import io.renren.modules.app.dto.HotSearchDto;
import io.renren.modules.app.dto.TaskBannerDto;
import io.renren.modules.app.entity.search.SearchHistoryEntity;
import io.renren.modules.app.entity.search.SearchLogEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.entity.task.TaskTagEntity;
import io.renren.modules.app.service.SearchService;
import io.renren.modules.app.service.TaskTagService;
import io.swagger.annotations.Authorization;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SearchServiceImpl extends ServiceImpl<SearchDao, SearchHistoryEntity> implements SearchService {
    private final static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Resource
    private SearchDao searchDao;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public void saveHistory(Long userId, String keyword) {

        if (existsHistory(userId, keyword)) {
            updateHistory(userId, keyword);
        } else {
            addHistory(userId, keyword);
        }
        addSearchLog(keyword);

    }


    @Override
    public List<SearchHistoryEntity> getHistories(Long userId) {
        Wrapper<SearchHistoryEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId)
                .orderBy("create_time", false);
        List<SearchHistoryEntity> histories = selectList(wrapper);
        if (CollectionUtils.isEmpty(histories)) {
            return new ArrayList<>();
        }
        return histories;
    }

    @Override
    public void clearHistories(Long[] historyIds) {
        if (ArrayUtils.isEmpty(historyIds)) {
            return;
        }
        List<SearchHistoryEntity> histories = this.selectBatchIds(Arrays.asList(historyIds));
        if (!CollectionUtils.isEmpty(histories)) {
            for (SearchHistoryEntity history : histories) {
                history.setDeleted(true);
            }
            this.updateBatchById(histories);
        }
    }

    @Override
    public List<HotSearchDto> getHotSearch() {
        //redis保存热门搜索列表，过期时间为1天
        List<HotSearchDto> hotSearchs =  redisUtils.getList(RedisKeys.HOT_SEARCH,HotSearchDto.class);
        if (CollectionUtils.isEmpty(hotSearchs)) {
            hotSearchs = searchDao.getHotSearch();
            if (!CollectionUtils.isEmpty(hotSearchs)) {
                redisUtils.addList(RedisKeys.HOT_SEARCH, hotSearchs);
            }
        }
        return hotSearchs;
    }



    /**
     * 添加搜索历史
     */
    private void addHistory(Long userId, String keyword) {
        SearchHistoryEntity history = new SearchHistoryEntity(userId, keyword, DateUtils.now());
        this.insert(history);
    }

    /**
     * 更新搜索历史
     */
    private void updateHistory(Long userId, String keyword) {
        SearchHistoryEntity history = new SearchHistoryEntity(userId, keyword, DateUtils.now());
        Wrapper<SearchHistoryEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("keyword", keyword);
        this.update(history, wrapper);
    }

    /**
     * 是否存在搜索历史
     */
    private boolean existsHistory(Long userId, String keyword) {
        Wrapper<SearchHistoryEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("keyword", keyword);
        return selectCount(wrapper) > 0;
    }

    /**
     * 添加搜索日志
     * @param keyword
     */
    private void addSearchLog(String keyword) {
        SearchLogEntity log = new SearchLogEntity(DateUtils.now(),keyword);
        searchDao.insertLog(log);
    }

}