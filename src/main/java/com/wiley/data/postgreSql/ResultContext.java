package com.wiley.data.postgreSql;

import java.math.BigDecimal;

public class ResultContext {
    private String item_id;
    private String status;
    private String calculated_score;
    private String points_earned;
    private String points_possible;
    private String id;
    private String learning_context_id;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCalculated_score() {
        return calculated_score;
    }

    public void setCalculated_score(String calculated_score) {
        this.calculated_score = calculated_score;
    }

    public String getPoints_earned() {
        return points_earned;
    }

    public void setPoints_earned(String points_earned) {
        this.points_earned = points_earned;
    }

    public String getPoints_possible() {
        return points_possible;
    }

    public void setPoints_possible(String points_possible) {
        this.points_possible = points_possible;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLearning_context_id() {
        return learning_context_id;
    }

    public void setLearning_context_id(String learning_context_id) {
        this.learning_context_id = learning_context_id;
    }

}
