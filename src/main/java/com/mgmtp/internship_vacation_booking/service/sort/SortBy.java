package com.mgmtp.internship_vacation_booking.service.sort;

public enum SortBy {
    NAME("name", "requestStatusEntityPK.requestByRequestId.employeeByEmployeeId.firstName"),
    SUBMITTED_DATE("submitted", "requestStatusEntityPK.requestByRequestId.createdAt"),
    REQUEST_TYPE("type", "requestStatusEntityPK.requestByRequestId.vacationType.name"),
    FROM_DATE("from", "requestStatusEntityPK.requestByRequestId.fromDate");

    private String value;
    private String property;

    public String getValue() {
        return value;
    }

    public String getProperty() {
        return property;
    }

    SortBy(String value, String property) {
        this.value = value;
        this.property = property;
    }
}
