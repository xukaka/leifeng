package io.renren.modules.app.entity;

public enum ScoreLevelEnum {


    oneStar(1),

    twoStar(2),

    threeStar(3),

    fourStar(4),
    fiveStar(5);


    private int score;

    ScoreLevelEnum(int score) {

        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
