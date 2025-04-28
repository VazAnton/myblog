package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Paging {

    private Long pageNumber;
    private Long pageSize;
    private Long total;

    public boolean hasPrevious() {
        return pageNumber > 1;
    }

    public boolean hasNext() {
        return pageNumber * pageSize < total;
    }

    public Long pageSize() {
        return pageSize;
    }

    public Long pageNumber() {
        return pageNumber;
    }
}
