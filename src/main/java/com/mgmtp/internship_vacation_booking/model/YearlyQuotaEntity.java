package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "yearly_quota")
@ToString
@Data
public class YearlyQuotaEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "quota")
    private Integer quota;

    @ManyToOne
    @JoinColumn(name = "vacation_type_id", nullable = false)
    private VacationTypeEntity vacationType;

    @OneToMany(mappedBy = "employeeYearlyQuotaEntityPK.yearlyQuota", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EmployeeYearlyQuotaEntity> employeeYearlyQuotaEntityList;

}
