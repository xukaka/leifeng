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

@ApiModel(value = "任务表单")
public class TaskForm {
    @ApiModelProperty(value = "任务id[创建时id不填，更新时id必填]", example = "")
    private Long id;
    @ApiModelProperty(value = "主题", example = "帮忙抢火车票")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "描述", example = "")
    @NotBlank(message = "描述")
    private String description;

    @ApiModelProperty(value = "图片url列表", example = "")
    private List<String> imageUrls;

    @ApiModelProperty(value = "标签id列表", example = "")
    private List<Long> tagIds;

    @ApiModelProperty(value = "任务开始时间", example = "")
    @NotNull
    private Long startTime;

    @ApiModelProperty(value = "任务过期时间", example = "")
    @NotNull
    private Long expireTime;

    @ApiModelProperty(value = "虚拟货币（雷锋币）", example = "")
    @NotNull
    private Integer virtualCurrency;

    @ApiModelProperty(value = "任务地址id", example = "")
    @NotNull
    private Long addressId;

    @ApiModelProperty(value = "被通知的用户id列表", example = "")
    private List<Long> notifiedUserIds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getVirtualCurrency() {
        return virtualCurrency;
    }

    public void setVirtualCurrency(Integer virtualCurrency) {
        this.virtualCurrency = virtualCurrency;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public List<Long> getNotifiedUserIds() {
        return notifiedUserIds;
    }

    public void setNotifiedUserIds(List<Long> notifiedUserIds) {
        this.notifiedUserIds = notifiedUserIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
