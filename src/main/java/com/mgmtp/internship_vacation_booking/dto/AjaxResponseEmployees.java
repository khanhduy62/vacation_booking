package com.mgmtp.internship_vacation_booking.dto;

import lombok.Data;

import java.util.List;

@Data
public class AjaxResponseEmployees {
    private List<Employee> employeeList;
    private String errorMessage;
}
