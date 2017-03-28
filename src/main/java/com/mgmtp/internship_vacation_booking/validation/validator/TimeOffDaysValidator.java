package com.mgmtp.internship_vacation_booking.validation.validator;

import com.mgmtp.internship_vacation_booking.dto.RequestStatusDetails;
import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import com.mgmtp.internship_vacation_booking.service.request.RequestService;
import com.mgmtp.internship_vacation_booking.service.yearly_quota.YearlyQuotaService;
import com.mgmtp.internship_vacation_booking.validation.annotation.ValidTimeOffDays;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeOffDaysValidator implements ConstraintValidator<ValidTimeOffDays, RequestStatusDetails> {

    @Autowired
    private YearlyQuotaService yearlyQuotaService;

    @Autowired
    private RequestService requestService;

    @Override
    public void initialize(ValidTimeOffDays validTimeOffDays) {}

    @Override
    public boolean isValid(RequestStatusDetails requestStatusDetails, ConstraintValidatorContext context) {
        int requestId = requestStatusDetails.getRequestId();
        VacationTypeEntity vacationType = requestService.getVacationTypeByRequestId(requestId);

        if(yearlyQuotaService.getRemainDays(vacationType,requestService.getRequestById(requestId).getEmployeeByEmployeeId().getId() ) != null) {
            int remainDays = yearlyQuotaService.getRemainDays(vacationType,requestService.getRequestById(requestId).getEmployeeByEmployeeId().getId());
            int durationDays = requestService.getDurationDaysByRequestId(requestId);
            if (requestStatusDetails.isApproved() && durationDays > remainDays) {
                return false;
            }
        }
        return true;
    }

}
