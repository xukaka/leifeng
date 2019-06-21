package io.renren.modules.app.entity;

public enum TaskDifficultyEnum {

    SIMPLE(1, 2000),//单位为RMB分

    NORMAL(2001, 10000),

    DIFFICULT(10001, Integer.MAX_VALUE);

    private int minMoney;

    private int maxMoney;

    TaskDifficultyEnum(int minMoney, int maxMoney) {
        this.minMoney = minMoney;
        this.maxMoney = maxMoney;
    }

    public int getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(int minMoney) {
        this.minMoney = minMoney;
    }

    public int getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(int maxMoney) {
        this.maxMoney = maxMoney;
    }
}
