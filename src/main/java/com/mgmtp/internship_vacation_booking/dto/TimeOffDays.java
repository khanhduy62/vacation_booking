package com.mgmtp.internship_vacation_booking.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TimeOffDays {
    private int yearlyQuotaId;
    private String vacationName;
    private String quota;
}
