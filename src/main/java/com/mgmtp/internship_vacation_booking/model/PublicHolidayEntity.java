package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "yearlyPublicHolidays")
@Entity
@Table(name = "public_holiday")
public class PublicHolidayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "key")
    private String key;

    @OneToMany(mappedBy = "publicHoliday")
    Set<YearlyPublicHolidayEntity> yearlyPublicHolidays;
}
