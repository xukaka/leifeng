package io.renren.modules.app.entity.search;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 搜索历史
 */
@TableName("t_search_history")
public class SearchHistoryEntity extends BaseEntity {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 关键字
     */
    private String keyword;

    public SearchHistoryEntity(Long userId, String keyword,Long createTime) {
        super(createTime);
        this.userId = userId;
        this.keyword = keyword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
