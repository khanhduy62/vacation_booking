package com.mgmtp.internship_vacation_booking.dto;

import com.mgmtp.internship_vacation_booking.validation.annotation.ValidTimeOffDays;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
@Data
@ValidTimeOffDays
public class RequestStatusDetails {

    @NotNull
    @Min(1)
    private int requestId;
    private int leaderId;
    private boolean approved;

}
