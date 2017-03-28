package com.mgmtp.internship_vacation_booking.service.request;

import com.mgmtp.internship_vacation_booking.dto.RequestForm;
import com.mgmtp.internship_vacation_booking.model.RequestEntity;
import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;

import java.util.Date;
import java.util.Locale;

public interface RequestService {

    RequestEntity saveRequest(RequestForm requestForm);

    VacationTypeEntity getVacationTypeByRequestId(int requestId);

    int getDurationDaysByRequestId(int requestId);

    RequestEntity getRequestById(int requestId);

    RequestEntity updateCancelled(int requestId);

    String getDurationMessage(Date from, Date to, Locale locale);
}
