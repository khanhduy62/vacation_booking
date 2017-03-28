package com.mgmtp.internship_vacation_booking.dto;

import lombok.Data;

@Data
public class AjaxResponseBody {
    public static int ERROR_INVALID_DAYS = 1;
    public static int ERROR_UPDATE_FAILURE = 2;

    private int errorCode;

    private String message;

}
