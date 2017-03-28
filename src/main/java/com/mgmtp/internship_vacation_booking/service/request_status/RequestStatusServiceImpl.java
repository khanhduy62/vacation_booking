package com.mgmtp.internship_vacation_booking.service.request_status;

import com.google.common.collect.Lists;
import com.mgmtp.internship_vacation_booking.dto.Approver;
import com.mgmtp.internship_vacation_booking.dto.EmployeeHistoryRequest;
import com.mgmtp.internship_vacation_booking.dto.RequestStatusDetails;
import com.mgmtp.internship_vacation_booking.model.RequestEntity;
import com.mgmtp.internship_vacation_booking.model.RequestStatusEntity;
import com.mgmtp.internship_vacation_booking.repository.RequestRepository;
import com.mgmtp.internship_vacation_booking.repository.RequestStatusRepository;
import com.mgmtp.internship_vacation_booking.service.CustomEmpDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class RequestStatusServiceImpl implements RequestStatusService {

    private RequestRepository requestRepository;
    private RequestStatusRepository requestStatusRepository;
    private CustomEmpDetailsService customEmpDetailsService;

    @Autowired
    public RequestStatusServiceImpl(RequestRepository requestRepository, RequestStatusRepository requestStatusRepository, CustomEmpDetailsService customEmpDetailsService) {
        this.requestRepository = requestRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.customEmpDetailsService = customEmpDetailsService;
    }

    private List<Approver> getApproversByRequestStatus(List<RequestStatusEntity> requestStatusEntities) {
        ArrayList<Approver> approvers = new ArrayList<>();
        for (RequestStatusEntity requestStatus : requestStatusEntities) {
            Approver approver = new Approver();
            approver.setName(requestStatus.getRequestStatusEntityPK().getEmployeeByLeaderId().getFullName());
            approver.setApproval(requestStatus.getApproved());
            approvers.add(approver);
        }
        return approvers;
    }

    private String getStatusString(List<Approver> approvers) {
        for (Approver approver : approvers) {
            if (approver.getApproval() == null)
                return Approver.PENDING;
            if (!approver.getApproval())
                return Approver.REJECT;
        }
        return Approver.APPROVED;
    }

    @Override
    public List<EmployeeHistoryRequest> getEmployeeHistories() {
        List<RequestEntity> requestEntities = requestRepository.findEmployeeByEmployeeId(customEmpDetailsService.getCurrentUser().getId());
        List<EmployeeHistoryRequest> employeeHistoryRequests = new ArrayList<>();
        requestEntities.sort(Comparator.comparing(RequestEntity::getCreatedAt));
        requestEntities = Lists.reverse(requestEntities);
        for (RequestEntity requestEntity : requestEntities) {
            EmployeeHistoryRequest historyRequest = new EmployeeHistoryRequest();
            historyRequest.setRequestId(requestEntity.getId());
            historyRequest.setFrom(requestEntity.getFromDate());
            historyRequest.setTo(requestEntity.getToDate());
            historyRequest.setVacationType(requestEntity.getVacationType().getKey());
            historyRequest.setComment(requestEntity.getComment());
            historyRequest.setSubmittedTime(requestEntity.getCreatedAt());

            Date today =  new Date();
            if(today.compareTo(requestEntity.getFromDate())>=0 || requestEntity.isCancelled())
                historyRequest.setCancelled(true);
            else
                historyRequest.setCancelled(false);

            List<Approver> approvers = getApproversByRequestStatus(requestEntity.getRequestStatusList());
            historyRequest.setApproval(getStatusString(approvers));

            if(historyRequest.isCancelled())
                historyRequest.setApproval("Cancelled");

            historyRequest.setApprovers(approvers);

            employeeHistoryRequests.add(historyRequest);
        }

        return employeeHistoryRequests;
    }

    @Override
    public boolean isApprovalRequest(RequestEntity request) {
        int count = 0;
        for (RequestStatusEntity requestStatus : request.getRequestStatusList()) {
            if (requestStatus.getApproved() == null || !requestStatus.getApproved()) {
                count = count - 1;
            } else {
                count = count + 1;
            }
        }
        return count > 0;
    }

    public boolean updateRequestStatus(RequestStatusDetails requestStatusDetails) {
        try {
            requestStatusRepository.updateByRequestIdAndLeaderId(requestStatusDetails.getRequestId(), requestStatusDetails.getLeaderId(), requestStatusDetails.isApproved());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
