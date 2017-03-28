package com.mgmtp.internship_vacation_booking.service.yearly_quota;

import com.mgmtp.internship_vacation_booking.dto.RemainingDayRow;
import com.mgmtp.internship_vacation_booking.dto.TimeOffDays;
import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import com.mgmtp.internship_vacation_booking.model.YearlyQuotaEntity;

import java.util.List;

public interface YearlyQuotaService {

    Integer getYearlyQuotaByVacationTypeAndYear(VacationTypeEntity vacationType, int year);

    List<RemainingDayRow> getRemainingDayRows();

    int getTotalTimeOffByVacationTypeAndYear(int employeeId,VacationTypeEntity vacationType, int year);

    Integer getRemainDays(VacationTypeEntity vacationType,int employeeId);

    List<TimeOffDays> getTimeOffDaysByYear(int currentYear);

    List<YearlyQuotaEntity> copyQuotaData(int selectedYear);
}
