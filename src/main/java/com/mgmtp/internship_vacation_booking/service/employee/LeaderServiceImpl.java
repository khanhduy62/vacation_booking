package com.mgmtp.internship_vacation_booking.service.employee;

import com.mgmtp.internship_vacation_booking.dto.AdminPendingRequest;
import com.mgmtp.internship_vacation_booking.model.RequestEntity;
import com.mgmtp.internship_vacation_booking.model.RequestStatusEntity;
import com.mgmtp.internship_vacation_booking.repository.RequestStatusRepository;
import com.mgmtp.internship_vacation_booking.service.sort.SortBy;
import com.mgmtp.internship_vacation_booking.service.sort.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderServiceImpl implements LeaderService {

    private final RequestStatusRepository requestStatusRepository;

    public LeaderServiceImpl(RequestStatusRepository requestStatusRepository) {
        this.requestStatusRepository = requestStatusRepository;
    }

    @Override
    public Page<AdminPendingRequest> findPendingRequestByLeaderId(int leaderId, Integer pageNumber, Integer size, SortBy sortBy, SortDirection sortDirection) {
        // PageNumber 1..n for human readable, but in PageRequest page begin with
        PageRequest pageRequest = new PageRequest(pageNumber - 1, size, new Sort(new Sort.Order(sortDirection.getDirection(), sortBy.getProperty())));
        Page<RequestStatusEntity> requestStatusEntityPage = requestStatusRepository.findByLeaderIdPage(leaderId, pageRequest);
        return new PageImpl<>(convertToDTOObject(requestStatusEntityPage.getContent()), pageRequest, requestStatusEntityPage.getTotalElements());
    }

    @Override
    public Page<AdminPendingRequest> findPendingRequestByLeaderId(int leaderId, String keyword, Integer pageNumber, Integer size, SortBy sortBy, SortDirection sortDirection) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, size, new Sort(new Sort.Order(sortDirection.getDirection(), sortBy.getProperty())));
        Page<RequestStatusEntity> requestStatusEntityPage = requestStatusRepository.findByLeaderIdAndFilterByFirstName(leaderId, keyword, pageRequest);
        return new PageImpl<>(convertToDTOObject(requestStatusEntityPage.getContent()), pageRequest, requestStatusEntityPage.getTotalElements());
    }

    private List<AdminPendingRequest> convertToDTOObject(List<RequestStatusEntity> requestStatusEntityList){
        List<AdminPendingRequest> adminPendingRequests = new ArrayList<>();
        for (RequestStatusEntity requestStatus: requestStatusEntityList) {
            RequestEntity request = requestStatus.getRequestStatusEntityPK().getRequestByRequestId();

            AdminPendingRequest pendingRequest = new AdminPendingRequest();
            pendingRequest.setRequestId(request.getId());
            pendingRequest.setFullName(request.getEmployeeByEmployeeId().getFullName());
            pendingRequest.setFromDate(request.getFromDate());
            pendingRequest.setToDate(request.getToDate());
            pendingRequest.setVacationType(request.getVacationType().getName());
            pendingRequest.setComment(request.getComment());
            pendingRequest.setSubmittedTime(request.getCreatedAt());

            // find in RequestStatus records have this request ID -> find other approvers to show
            pendingRequest.setListSameRequestId(requestStatusRepository.findByRequestId(request.getId()));
            adminPendingRequests.add(pendingRequest);
        }

        return adminPendingRequests;
    }
}
