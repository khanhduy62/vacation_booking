package com.mgmtp.internship_vacation_booking.service.request_status;

import com.mgmtp.internship_vacation_booking.dto.EmployeeHistoryRequest;
import com.mgmtp.internship_vacation_booking.dto.RequestStatusDetails;
import com.mgmtp.internship_vacation_booking.model.RequestEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestStatusService {

    boolean isApprovalRequest(RequestEntity request);

    List<EmployeeHistoryRequest> getEmployeeHistories();

    boolean updateRequestStatus(RequestStatusDetails requestStatusDetails);
}
