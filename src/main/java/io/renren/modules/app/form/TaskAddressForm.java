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

@ApiModel(value = "任务地址表单")
public class TaskAddressForm {

    @ApiModelProperty(value = "地址id", example = "")
    private Long id;

    @ApiModelProperty(value = "创建人id", example = "")
    private Long creatorId;

    @ApiModelProperty(value = "姓名", example = "刘江舟")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "手机号码", example = "15013686205")
    @NotBlank
    private String mobile;

    @ApiModelProperty(value = "省", example = "广东省")
    @NotBlank
    private String province;

    @ApiModelProperty(value = "市", example = "深圳市")
    @NotBlank
    private String city;

    @ApiModelProperty(value = "区", example = "南山区")
    @NotBlank
    private String district;

    @ApiModelProperty(value = "街道", example = "沙河街道")
    @NotBlank
    private String street;

    @ApiModelProperty(value = "详细地址", example = "上白石43栋601")
    private String detail;

    @ApiModelProperty(value = "维度", example = "")
    @NotNull
    private Double latitude;
    @ApiModelProperty(value = "经度", example = "")
    @NotNull
    private Double longitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
}
