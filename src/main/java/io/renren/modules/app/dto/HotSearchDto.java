package io.renren.modules.app.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 热门搜索
 */
public class HotSearchDto implements Serializable {

	private String keyword;

	private Long searchCount;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Long searchCount) {
		this.searchCount = searchCount;

	}
}
