package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "employee_yearly_quota")
public class EmployeeYearlyQuotaEntity {

    @Column(name = "new_quota")
    private Integer newQuota;

    @EmbeddedId
    private EmployeeYearlyQuotaEntityPK employeeYearlyQuotaEntityPK;

}
