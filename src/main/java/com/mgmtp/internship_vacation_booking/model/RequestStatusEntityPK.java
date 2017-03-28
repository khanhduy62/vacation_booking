package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class RequestStatusEntityPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private RequestEntity requestByRequestId;

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    private EmployeeEntity employeeByLeaderId;

}
