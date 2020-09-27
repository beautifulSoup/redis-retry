package com.tangokk.redisretry.springbootexample.dto;

public class RetryItem {

    String key;
    public RetryItem() {
    }


    public RetryItem(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
