package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;

@Data
@EqualsAndHashCode(exclude = "publicHoliday")
@Entity
@Table(name = "yearly_public_holiday")
public class YearlyPublicHolidayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "from_date")
    private Date fromDate;

    @Basic
    @Column(name = "to_date")
    private Date toDate;

    @ManyToOne
    @JoinColumn(name = "public_holiday_id", referencedColumnName = "id")
    PublicHolidayEntity publicHoliday;
}
