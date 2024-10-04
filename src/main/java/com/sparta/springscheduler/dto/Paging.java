package com.sparta.springscheduler.dto;

import lombok.Getter;

@Getter
public class Paging {
    private int pageNumber; // 페이지 번호
    private int pageSize;   // 페이지 크기

    public Paging(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

}
