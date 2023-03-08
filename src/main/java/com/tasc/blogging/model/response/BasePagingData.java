package com.tasc.blogging.model.response;

import lombok.Data;

@Data
public class BasePagingData<T> {

    private int currentPage;
    private int size;
    private long totalPage;
    private long totalItem;
    private T content;

    public BasePagingData(int currentPage, int size, long totalPage, long totalItem, T content) {
        this.currentPage = currentPage;
        this.size = size;
        this.totalPage = totalPage;
        this.totalItem = totalItem;
        this.content = content;
    }
}
