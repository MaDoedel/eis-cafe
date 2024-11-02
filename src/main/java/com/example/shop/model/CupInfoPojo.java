package com.example.shop.model;

public class CupInfoPojo {
    private Long cupId;
    private Long ingredientId;
    private Long count;

    public CupInfoPojo(Long cupId, Long ingredientId, Long count) {
        this.cupId = cupId;
        this.ingredientId = ingredientId;
        this.count = count;
    }

    public Long getCupId() {
        return cupId;
    }

    public void setCupId(Long cupId) {
        this.cupId = cupId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}