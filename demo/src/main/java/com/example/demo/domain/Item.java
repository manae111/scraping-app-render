package com.example.demo.domain;

public class Item {

    private Integer id;
    private String url;
    private String itemName;
    private Integer priceOriginal;
    private Integer priceLatest;
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(Integer priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public Integer getPriceLatest() {
        return priceLatest;
    }

    public void setPriceLatest(Integer priceLatest) {
        this.priceLatest = priceLatest;
    }  
}
