package com.mgmtp.internship_vacation_booking.service.request;

import com.mgmtp.internship_vacation_booking.dto.RequestForm;
import com.mgmtp.internship_vacation_booking.dto.YearlyPublicHoliday;
import com.mgmtp.internship_vacation_booking.model.*;
import com.mgmtp.internship_vacation_booking.repository.RequestRepository;
import com.mgmtp.internship_vacation_booking.repository.RequestStatusRepository;
import com.mgmtp.internship_vacation_booking.service.CustomEmpDetailsService;
import com.mgmtp.internship_vacation_booking.service.email.EmailService;
import com.mgmtp.internship_vacation_booking.service.public_holiday.PublicHolidayService;
import com.mgmtp.internship_vacation_booking.service.vacation_type.VacationTypeService;
import com.mgmtp.internship_vacation_booking.util.JodaTimeService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EmailService emailService;
    private final VacationTypeService vacationTypeService;
    private final CustomEmpDetailsService customEmpDetailsService;
    private final RequestStatusRepository requestStatusRepository;
    private final PublicHolidayService publicHolidayService;
    private final MessageSource messageSource;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, EmailService emailService, VacationTypeService vacationTypeService,
                              CustomEmpDetailsService customEmpDetailsService, RequestStatusRepository requestStatusRepository, PublicHolidayService publicHolidayService, MessageSource messageSource) {
        this.emailService = emailService;
        this.requestRepository = requestRepository;
        this.vacationTypeService = vacationTypeService;
        this.customEmpDetailsService = customEmpDetailsService;
        this.requestStatusRepository = requestStatusRepository;
        this.publicHolidayService = publicHolidayService;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public RequestEntity saveRequest(RequestForm requestForm) {
        emailService.sendEmail("caophihung91@gmail.com");

        RequestEntity request = new RequestEntity();

        request.setFromDate(requestForm.getFromDate());
        request.setToDate(requestForm.getToDate());
        request.setComment(requestForm.getComment());
        request.setCreatedAt(Calendar.getInstance().getTime());
        request.setVacationType(vacationTypeService.getVacationTypeEntity(requestForm.getVacationType().getId()));
        request.setEmployeeByEmployeeId(customEmpDetailsService.getCurrentUser());

        RequestEntity requestEntitySubmitted = requestRepository.save(request);

        for (EmployeeEntity approver : customEmpDetailsService.getCurrentUser().getApprovers()) {
            RequestStatusEntity requestStatus = new RequestStatusEntity();
            RequestStatusEntityPK requestStatusEntityPK = new RequestStatusEntityPK();
            requestStatusEntityPK.setRequestByRequestId(requestEntitySubmitted);
            requestStatusEntityPK.setEmployeeByLeaderId(approver);
            requestStatus.setRequestStatusEntityPK(requestStatusEntityPK);
            requestStatus.setApproved(null);

            requestStatusRepository.save(requestStatus);
        }

        return requestEntitySubmitted;
    }

    @Override
    public VacationTypeEntity getVacationTypeByRequestId(int requestId) {
        return requestRepository.findOne(requestId).getVacationType();
    }

    @Override
    public int getDurationDaysByRequestId(int requestId) {
        RequestEntity requestEntity = requestRepository.findOne(requestId);
        return JodaTimeService.getWorkDaysFromTwoDates(requestEntity.getFromDate(), requestEntity.getToDate());
    }

    @Override
    public RequestEntity getRequestById(int requestId) {
        return requestRepository.findOne(requestId);
    }

    @Transactional
    @Override
    public RequestEntity updateCancelled(int requestId) {
        RequestEntity requestEntity = requestRepository.findOne(requestId);
        requestEntity.setCancelled(true);
        return requestRepository.save(requestEntity);
    }

    @Override
    public String getDurationMessage(Date from, Date to, Locale locale) {
        StringBuilder result = new StringBuilder();
        DateTime dt1 = new DateTime(from);
        DateTime dt2 = new DateTime(to);
        int duration = 0;
        Set<YearlyPublicHoliday> holidays = new HashSet<>();
        while(dt1.compareTo(dt2) <= 0 ){
            if(dt1.getDayOfWeek() !=  DateTimeConstants.SATURDAY && dt1.getDayOfWeek() !=  DateTimeConstants.SUNDAY){
                duration = duration +1;
                for (YearlyPublicHoliday yearlyPublicHoliday : publicHolidayService.getCurrentYearPublicHolidays()) {
                    if(JodaTimeService.isWithinRange(dt1, yearlyPublicHoliday.getFromDate(), yearlyPublicHoliday.getToDate())) {
                        holidays.add(yearlyPublicHoliday);
                        duration = duration -1;
                    }
                }
            }
            dt1 = dt1.plusDays(1);
        }
        result.append(duration);
        result.append(" " + messageSource.getMessage("day(s)",null, locale) + " ");

        if(holidays.size() > 0) {
            result.append(" " + messageSource.getMessage("excluding",null, locale) + " ");
            DateTime startDate = new DateTime(from);
            for (YearlyPublicHoliday yearlyPublicHoliday : holidays) {
                DateTime fromDateHoliday =  new DateTime(yearlyPublicHoliday.getFromDate());
                DateTime toDateHoliday =  new DateTime(yearlyPublicHoliday.getToDate());
                String dayFor = messageSource.getMessage("day_for",null, locale);
                String publicHolidayName = messageSource.getMessage(yearlyPublicHoliday.getKey(),null, locale);

                if(fromDateHoliday.isBefore(startDate)) {
                    fromDateHoliday = startDate;
                }
                if(toDateHoliday.isAfter(dt2)) {
                    toDateHoliday = dt2;
                }
                int days = Days.daysBetween(fromDateHoliday, toDateHoliday).getDays() + 1;

                result.append(days + " " + dayFor + " " + publicHolidayName + ", ");
            }
            result.deleteCharAt(result.length()-2);
        }
        return result.toString();
    }
}
