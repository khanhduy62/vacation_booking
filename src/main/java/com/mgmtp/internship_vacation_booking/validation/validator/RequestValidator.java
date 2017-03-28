package com.mgmtp.internship_vacation_booking.validation.validator;

import com.mgmtp.internship_vacation_booking.dto.RequestForm;
import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import com.mgmtp.internship_vacation_booking.service.CustomEmpDetailsService;
import com.mgmtp.internship_vacation_booking.service.vacation_type.VacationTypeService;
import com.mgmtp.internship_vacation_booking.service.yearly_quota.YearlyQuotaService;
import com.mgmtp.internship_vacation_booking.util.JodaTimeService;
import com.mgmtp.internship_vacation_booking.validation.annotation.Request;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

public class RequestValidator implements ConstraintValidator<Request, RequestForm> {

    @Autowired
    private YearlyQuotaService yearlyQuotaService;

    @Autowired
    private VacationTypeService vacationTypeService;

    @Autowired

    private CustomEmpDetailsService customEmpDetailsService;

    @Override
    public void initialize(Request constraintAnnotation) {

    }

    @Override
    public boolean isValid(RequestForm requestForm, ConstraintValidatorContext context) {
        if (requestForm == null)
            return false;

        Date fromDate = requestForm.getFromDate();
        Date toDate = requestForm.getToDate();
        int currentYear = Year.now().getValue();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");

        if (fromDate == null) {
            return false;
        }

        if (toDate == null)
            return false;

        Date tempNow = new Date();
        @SuppressWarnings("deprecation")
        Date now = new Date(tempNow.getYear(), tempNow.getMonth(), tempNow.getDate());
        if (fromDate.compareTo(now) < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{RequestValidator.requestForm.fromDate}").addPropertyNode("fromDate").addConstraintViolation();
            return false;
        }

        if (fromDate.compareTo(toDate) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{RequestValidator.requestForm.toDate}").addPropertyNode("toDate").addConstraintViolation();
            return false;
        }

        VacationTypeEntity vacationType = vacationTypeService.getVacationTypeEntity(requestForm.getVacationType().getId());
        String getYearTimeOffFromDate = df.format(fromDate);
        String getYearTimeOffToDate = df.format(toDate);

        int timeOffDays = JodaTimeService.getWorkDaysFromTwoDates(fromDate, toDate);
        int yearTimeOffFromDate = (Integer.parseInt(getYearTimeOffFromDate));
        int yearTimeOffToDate = (Integer.parseInt(getYearTimeOffToDate));

        if (yearTimeOffFromDate > currentYear){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{RequestValidator.requestForm.currentYear}").addPropertyNode("fromDate").addConstraintViolation();
            return false;
        }

        if (yearTimeOffToDate > currentYear){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{RequestValidator.requestForm.currentYear}").addPropertyNode("toDate").addConstraintViolation();
            return false;
        }

        if ( yearlyQuotaService.getRemainDays(vacationType, customEmpDetailsService.getCurrentUser().getId()) != null) {
            if(timeOffDays > yearlyQuotaService.getRemainDays(vacationType,customEmpDetailsService.getCurrentUser().getId())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{RequestValidator.requestForm.remainingDays}").addPropertyNode("toDate").addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
