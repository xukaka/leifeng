package io.renren.modules.app.form;

import io.renren.modules.app.entity.story.DiaryContentEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author huangshishui
 * @date 2019/4/18 22:39
 **/
@ApiModel("雷锋日记")
public class DiaryForm {

    @NotEmpty
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("日记内容列表")
    @NotNull
    private List<DiaryContentForm> contents;

    @NotNull
    @ApiModelProperty("日记是否公开")
    private Boolean isOpen;


    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DiaryContentForm> getContents() {
        return contents;
    }

    public void setContents(List<DiaryContentForm> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "DiaryForm{" +
                "title='" + title + '\'' +
                ", contents=" + contents +
                ", isOpen=" + isOpen +
                '}';
    }
}
