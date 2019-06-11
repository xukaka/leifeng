package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

@ApiModel
public class PageWrapper extends HashMap<String, Object> {

    private int currPage = 1;

    private int pageSize = 10;

    private int offset;

    private int limit;


    public PageWrapper(Map<String, Object> params) {
        this.putAll(params);

        //分页参数
        if (params.get("page") != null) {
            currPage = (int) params.get("page");
        }

        if (params.get("size") != null) {
            pageSize = (int) params.get("size");
        }

        this.put("offset", (currPage - 1) * pageSize);
        this.put("limit", pageSize);

        offset = (currPage - 1) * pageSize;
        limit = pageSize;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
