package com.mgmtp.internship_vacation_booking.dto;

import lombok.Data;

import java.util.List;

@Data
public class AjaxResponseErrorField {
    private int errorCode;
    private List<ErrorDescription> errorDescriptions;
    public static final int INVALID_PARAMETERS = 1;

    @Data
    public static class ErrorDescription {
        private String fieldName;
        private String message;
    }
}
