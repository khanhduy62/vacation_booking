package com.mgmtp.internship_vacation_booking.service.sort;

import org.springframework.stereotype.Service;

@Service
public class SortServiceImpl implements SortService {
    @Override
    public SortBy getSortByEnum(String sortByString) {
        SortBy[] sortByList = SortBy.values();
        for (SortBy sortBy : sortByList) {
            if (sortBy.getValue().equalsIgnoreCase(sortByString))
                return sortBy;
        }
        return SortBy.SUBMITTED_DATE;
    }

    @Override
    public SortDirection getSortDirectionEnum(String sortDirectionString) {
        SortDirection[] sortDirections = SortDirection.values();
        for (SortDirection sortDirection : sortDirections) {
            if (sortDirection.getValue().equalsIgnoreCase(sortDirectionString))
                return sortDirection;
        }
        return SortDirection.DESC;
    }
}
