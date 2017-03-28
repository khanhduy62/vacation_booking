package com.mgmtp.internship_vacation_booking.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Approver {
    public static final String REJECT = "Rejected";
    public static final String APPROVED = "Approved";
    public static final String PENDING = "Pending";

    private String name;
    private Boolean approval;
}

