package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务标签表
 */
@TableName("t_task_tag")
public class TagEntity extends BaseEntity {

    /**
     * 标签名称
     */
    private String name;
    /**
     * 使用次数
     */
    private Long usageCount = 0L;

    public TagEntity() {
    }

    public TagEntity(Long createTime, String name) {
        super(createTime);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }
}
