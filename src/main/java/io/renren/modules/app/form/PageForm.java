package io.renren.modules.app.form;

import java.io.Serializable;

public class PageForm implements Serializable {

    private int curPage = 1;

    private int pageSize = 10;

    private int offset = 0;

    private int limit = 10;

    public PageForm(Integer curPage, Integer pageSize) {
        //分页参数
        if (curPage != null && curPage > 0) {
            this.curPage = curPage;
            this.offset = (curPage - 1) * pageSize;
        }

        if (pageSize != null && pageSize > 0) {
            this.pageSize = pageSize;
            this.limit = pageSize;
        }
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

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
