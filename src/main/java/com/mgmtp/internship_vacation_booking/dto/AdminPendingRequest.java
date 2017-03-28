package com.mgmtp.internship_vacation_booking.dto;

import com.mgmtp.internship_vacation_booking.model.RequestStatusEntity;
import com.mgmtp.internship_vacation_booking.util.JodaTimeService;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ToString
@Data
public class AdminPendingRequest implements Serializable {

    private int requestId;

    private String fullName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fromDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date toDate;

    private String vacationType;

    private String comment;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date submittedTime;

    private List<RequestStatusEntity> listSameRequestId;

    public long getDays() {
        return JodaTimeService.getWorkDaysFromTwoDates(fromDate, toDate);
    }
}
