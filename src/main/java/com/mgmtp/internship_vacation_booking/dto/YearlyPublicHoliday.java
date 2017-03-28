package com.mgmtp.internship_vacation_booking.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ToString
@Data
public class YearlyPublicHoliday {

    @NotNull
    private String name;

    private String description;

    private String key;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fromDate;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date toDate;
}
