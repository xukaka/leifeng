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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "任务圈表单")
public class TaskCircleForm {

    @ApiModelProperty(value = "ID", example = "")
    private Long id;

    @ApiModelProperty(value = "圈名称", example = "")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "圈头像", example = "")
    private String avatar;

    @ApiModelProperty(value = "圈描述", example = "")
    @NotBlank
    private String description;

    @ApiModelProperty(value = "是否需要审核", example = "")
    @NotNull
    private Boolean needReview;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getNeedReview() {
        return needReview;
    }

    public void setNeedReview(Boolean needReview) {
        this.needReview = needReview;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
