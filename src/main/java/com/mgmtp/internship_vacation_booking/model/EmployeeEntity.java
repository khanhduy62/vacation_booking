package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"roles", "leader", "approvers"})
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "start_working_date")
    private Date startWorkingDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "employee_role",
            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<RoleEntity> roles;

    @ManyToOne
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private EmployeeEntity leader;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "employee_approver",
            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "approver_id", referencedColumnName = "id"))
    private Set<EmployeeEntity> approvers;

    public EmployeeEntity(EmployeeEntity emp) {
        this.id = emp.id;
        this.firstName = emp.firstName;
        this.lastName = emp.lastName;
        this.email = emp.email;
        this.password = emp.password;
        this.startWorkingDate = emp.startWorkingDate;
    }

    @Transient
    public String getFullName(){
        return firstName + ' ' + lastName;
    }
}
