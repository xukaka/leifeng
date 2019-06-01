package io.renren.modules.app.dto;

import java.io.Serializable;

/**
 * 评分面板
 */
public class ScoreBoardDto implements Serializable {

    //一星数量
    private Integer oneStarNum=0;
    private Integer twoStarNum=0;
    private Integer threeStarNum=0;
    private Integer fourStarNum=0;
    private Integer fiveStarNum=0;


    //综合评分
    private double scoreAverage=0f;

    //评分总次数
    private Integer scoreTotalNum=0;

    public Integer getOneStarNum() {
        return oneStarNum;
    }

    public void setOneStarNum(Integer oneStarNum) {
        this.oneStarNum = oneStarNum;
    }

    public Integer getTwoStarNum() {
        return twoStarNum;
    }

    public void setTwoStarNum(Integer twoStarNum) {
        this.twoStarNum = twoStarNum;
    }

    public Integer getThreeStarNum() {
        return threeStarNum;
    }

    public void setThreeStarNum(Integer threeStarNum) {
        this.threeStarNum = threeStarNum;
    }

    public Integer getFourStarNum() {
        return fourStarNum;
    }

    public void setFourStarNum(Integer fourStarNum) {
        this.fourStarNum = fourStarNum;
    }

    public Integer getFiveStarNum() {
        return fiveStarNum;
    }

    public void setFiveStarNum(Integer fiveStarNum) {
        this.fiveStarNum = fiveStarNum;
    }

    public double getScoreAverage() {
        return scoreAverage;
    }

    public void setScoreAverage(double scoreAverage) {
        this.scoreAverage = scoreAverage;
    }

    public Integer getScoreTotalNum() {
        return scoreTotalNum;
    }

    public void setScoreTotalNum(Integer scoreTotalNum) {
        this.scoreTotalNum = scoreTotalNum;
    }
}
