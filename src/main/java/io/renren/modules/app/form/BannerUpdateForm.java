package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel("横幅更新参数对象")
public class BannerUpdateForm extends BannerForm{

    @NotNull
    @ApiModelProperty("id")
    private Long id;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
