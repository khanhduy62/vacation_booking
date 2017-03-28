package com.mgmtp.internship_vacation_booking.dto;

import com.mgmtp.internship_vacation_booking.util.JodaTimeService;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


@ToString
@Data
public class EmployeeHistoryRequest {

    private int requestId;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date from;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date to;

    private String vacationType;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date submittedTime;

    private String comment;

    private String approval;

    private List<Approver> approvers;

    private boolean isCancelled;

    public long getDays() {
        return JodaTimeService.getWorkDaysFromTwoDates(from, to);
    }
}
