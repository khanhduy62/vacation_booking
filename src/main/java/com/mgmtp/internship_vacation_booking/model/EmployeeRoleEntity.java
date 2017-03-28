package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Jango on 12/13/2016.
 */
@Data
@Entity
@Table(name = "employee_role")
public class EmployeeRoleEntity implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity emp;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

}
