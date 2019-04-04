package io.renren.modules.app.entity.search;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 搜索日志记录
 */
@TableName("t_search_log")
public class SearchLogEntity extends BaseEntity {

    /**
     * 关键字
     */
    private String keyword;

    public SearchLogEntity(String keyword) {
        this.keyword = keyword;
    }

    public SearchLogEntity(Long createTime, String keyword) {
        super(createTime);
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
