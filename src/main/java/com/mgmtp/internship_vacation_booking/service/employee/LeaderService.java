package com.mgmtp.internship_vacation_booking.service.employee;

import com.mgmtp.internship_vacation_booking.dto.AdminPendingRequest;
import com.mgmtp.internship_vacation_booking.service.sort.SortBy;
import com.mgmtp.internship_vacation_booking.service.sort.SortDirection;
import org.springframework.data.domain.Page;

public interface LeaderService {
    /**
     *
     * @param leaderId Leader Id to get pending requests
     * @param pageNumber Page number to show
     * @param size Number of rows in one page
     * @param sortBy Sort type, it can be submitted, name, from date and type of request  @return Page collection
     * @param sortDirection Sort direction, it can be either desc or asc
     */
    Page<AdminPendingRequest> findPendingRequestByLeaderId(int leaderId, Integer pageNumber, Integer size, SortBy sortBy, SortDirection sortDirection);
    Page<AdminPendingRequest> findPendingRequestByLeaderId(int leaderId, String keyword, Integer pageNumber, Integer size, SortBy sortBy, SortDirection sortDirection);
}
