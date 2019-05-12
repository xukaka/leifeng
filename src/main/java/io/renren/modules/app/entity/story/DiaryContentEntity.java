package io.renren.modules.app.entity.story;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;
import io.renren.modules.app.entity.ParagraphTypeEnum;

@TableName("t_diary_content")
public class DiaryContentEntity  extends BaseEntity {

    /**
     * 日记id
     */
    private Long diaryId;

    /**
     * 段落排序
     */
    private Integer paragraphSort;
    /**
     * 段落
     */
    private String paragraph;

    /**
     * 段落类型
     */
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

    public Long getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(Long diaryId) {
        this.diaryId = diaryId;
    }
}
