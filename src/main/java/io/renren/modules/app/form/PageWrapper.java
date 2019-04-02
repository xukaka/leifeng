package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@ApiModel
public class PageWrapper extends HashMap {

    private int currPage=1;

    private int pageSize=10;

    public PageWrapper(Map params){
        this.putAll(params);

        //分页参数
        if(params.get("page") != null){
            currPage = Integer.parseInt((String)params.get("page"));
        }

        if(params.get("size") != null){
            pageSize = Integer.parseInt((String)params.get("size"));
        }

        this.put("offset",(currPage-1)*pageSize);
        this.put("limit",pageSize);
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
}
