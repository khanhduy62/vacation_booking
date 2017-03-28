package com.mgmtp.internship_vacation_booking.dto;

import com.mgmtp.internship_vacation_booking.validation.annotation.ValidSuperAdminRequest;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@ToString
@Data
@ValidSuperAdminRequest
public class SuperAdminRequest {

    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer employeeId;

    private List<Integer> approverList;

    private String approverErrorField;
}

