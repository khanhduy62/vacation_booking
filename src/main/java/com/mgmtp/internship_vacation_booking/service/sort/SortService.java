package com.mgmtp.internship_vacation_booking.service.sort;

public interface SortService {
    SortBy getSortByEnum(String sortByString);

    SortDirection getSortDirectionEnum(String sortDirectionString);
}
