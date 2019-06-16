package io.renren.modules.app.dto;

import java.io.Serializable;

//统计经验值和虚拟币
public class ExperienceAndVirtualCurrencyDto implements Serializable {

    private Integer totalExperience=0;//总经验值
    private Integer totalVirtualCurrency=0;//总虚拟币

    public Integer getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(Integer totalExperience) {
        this.totalExperience = totalExperience;
    }

    public Integer getTotalVirtualCurrency() {
        return totalVirtualCurrency;
    }

    public void setTotalVirtualCurrency(Integer totalVirtualCurrency) {
        this.totalVirtualCurrency = totalVirtualCurrency;
    }
}
