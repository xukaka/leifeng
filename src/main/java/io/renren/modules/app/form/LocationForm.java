package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel
public class LocationForm {

    @ApiModelProperty(value = "用户的id")
    @NotBlank
    private Long id;

    @ApiModelProperty(value = "维度")
    private Double lat;

    @ApiModelProperty(value = "经度")
    private Double lng;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


}
