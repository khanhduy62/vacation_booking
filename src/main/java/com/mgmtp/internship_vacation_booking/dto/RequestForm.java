package com.mgmtp.internship_vacation_booking.dto;

import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import com.mgmtp.internship_vacation_booking.validation.annotation.Request;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@ToString
@Data
@Request
public class RequestForm {

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fromDate;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date toDate;

    @Size(max = 255)
    private String comment;

    @NotNull
    private VacationTypeEntity vacationType;

    public RequestForm() {
    }

}
