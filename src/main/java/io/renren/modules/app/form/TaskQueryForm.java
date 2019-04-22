/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.app.form;

import io.renren.modules.app.entity.TaskDifficultyEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "任务查询表单")
public class TaskQueryForm {

    @ApiModelProperty(value = "圈id", example = "")
    private Long circleId;
    @ApiModelProperty(value = "关键字", example = "")
    private String keyword;
    @ApiModelProperty(value = "当前位置经度", example = "")
    @NotNull
    private Double longitude;
    @ApiModelProperty(value = "当前位置维度", example = "")
    @NotNull
    private Double latitude;
    @ApiModelProperty(value = "范围半径[单位m]", example = "")
    @NotNull
    private Integer raidus;
    @ApiModelProperty(value = "标签id列表", example = "")
    private List<Long> tagIds;
    @ApiModelProperty(value = "任务难度", example = "")
    private TaskDifficultyEnum taskDifficulty;
    @ApiModelProperty(value = "当前页", example = "")
    private Integer curPage;
    @ApiModelProperty(value = "分页大小", example = "")
    private Integer pageSize;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRaidus() {
        return raidus;
    }

    public void setRaidus(Integer raidus) {
        this.raidus = raidus;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public TaskDifficultyEnum getTaskDifficulty() {
        return taskDifficulty;
    }

    public void setTaskDifficulty(TaskDifficultyEnum taskDifficulty) {
        this.taskDifficulty = taskDifficulty;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCircleId() {
        return circleId;
    }

    public void setCircleId(Long circleId) {
        this.circleId = circleId;
    }
}
