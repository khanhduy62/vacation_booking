package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class EmployeeYearlyQuotaEntityPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "yearly_quota_id", nullable = false)
    private YearlyQuotaEntity yearlyQuota;

}
