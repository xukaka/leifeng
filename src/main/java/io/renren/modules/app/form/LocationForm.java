package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LocationForm {

/*    @ApiModelProperty(value = "用户的id")
    @NotBlank
    private Long memid;*/

    @ApiModelProperty(value = "维度")
    private Double lat;

    @ApiModelProperty(value = "经度")
    private Double lng;

   /* public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLng() {
        return lng;
    }
}
