package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vacation_type")
public class VacationTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "depend_on_start_working_date")
    private boolean dependOnStartWorkingDate;

    @Column(name = "key")
    private String key;

    @Column(name = "display_order")
    private Integer displayOrder;

}
