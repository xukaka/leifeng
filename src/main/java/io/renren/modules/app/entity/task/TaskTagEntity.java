package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务标签表
 */
@TableName("t_task_tag")
public class TaskTagEntity extends BaseEntity {

    /**
     * 标签名称
     */
    private String name;
    /**
     * 使用次数
     */
    private Long usageCount;

    public TaskTagEntity() {
    }

    public TaskTagEntity(String name) {
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
