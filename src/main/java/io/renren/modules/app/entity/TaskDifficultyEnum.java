package io.renren.modules.app.entity;

public enum TaskDifficultyEnum {

    FREE(Integer.MIN_VALUE, 0),

    SIMPLE(1, 20),

    NORMAL(21, 50),

    DIFFICULT(51, Integer.MAX_VALUE);

    private int minVirtualCurrency;

    private int maxVirtualCurrency;

    TaskDifficultyEnum(int minVirtualCurrency, int maxVirtualCurrency) {
        this.minVirtualCurrency = minVirtualCurrency;
        this.maxVirtualCurrency = maxVirtualCurrency;
    }

    public int getMinVirtualCurrency() {
        return minVirtualCurrency;
    }

    public void setMinVirtualCurrency(int minVirtualCurrency) {
        this.minVirtualCurrency = minVirtualCurrency;
    }

    public int getMaxVirtualCurrency() {
        return maxVirtualCurrency;
    }

    public void setMaxVirtualCurrency(int maxVirtualCurrency) {
        this.maxVirtualCurrency = maxVirtualCurrency;
    }
}
