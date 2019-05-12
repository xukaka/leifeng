package io.renren.modules.app.form;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;
import io.renren.modules.app.entity.ParagraphTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("日记段落")
public class DiaryContentForm {


    @ApiModelProperty("段落排序")
    @NotNull
    private Integer paragraphSort;
    @ApiModelProperty("段落")
    @NotEmpty
    private String paragraph;
    @ApiModelProperty("段落类型")
    @NotEmpty
    private ParagraphTypeEnum type;

    public Integer getParagraphSort() {
        return paragraphSort;
    }

    public void setParagraphSort(Integer paragraphSort) {
        this.paragraphSort = paragraphSort;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public ParagraphTypeEnum getType() {
        return type;
    }

    public void setType(ParagraphTypeEnum type) {
        this.type = type;
    }
}
