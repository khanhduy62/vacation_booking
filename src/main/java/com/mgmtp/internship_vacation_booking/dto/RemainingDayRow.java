package com.mgmtp.internship_vacation_booking.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class RemainingDayRow {

    private String vacationType;

    private Integer yearlyQuota;

    private Integer remainingDays;
}
