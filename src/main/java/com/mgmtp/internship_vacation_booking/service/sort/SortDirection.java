package com.mgmtp.internship_vacation_booking.service.sort;


import org.springframework.data.domain.Sort;

public enum SortDirection {
    ASC("asc", Sort.Direction.ASC),
    DESC("desc", Sort.Direction.DESC);

    private String value;
    private Sort.Direction direction;

    public String getValue() {
        return value;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    SortDirection(String value, Sort.Direction direction) {
        this.value = value;
        this.direction = direction;
    }
}
