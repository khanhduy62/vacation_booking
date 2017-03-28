package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@EqualsAndHashCode(exclude = {"employeeByEmployeeId"})
@Table(name = "request")
@Data
public class RequestEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "from_date", nullable = false)
    private Date fromDate;

    @Column(name = "to_date", nullable = false)
    private Date toDate;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "comment")
    private String comment;

    @Column(name = "cancelled")
    private boolean cancelled;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employeeByEmployeeId;

    @ManyToOne
    @JoinColumn(name = "vacation_type_id", nullable = false)
    private VacationTypeEntity vacationType;

    @OneToMany(mappedBy = "requestStatusEntityPK.requestByRequestId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RequestStatusEntity> requestStatusList;

}
