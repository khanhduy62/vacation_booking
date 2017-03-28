package com.mgmtp.internship_vacation_booking.service.yearly_quota;

import com.mgmtp.internship_vacation_booking.dto.RemainingDayRow;
import com.mgmtp.internship_vacation_booking.dto.TimeOffDays;
import com.mgmtp.internship_vacation_booking.model.RequestEntity;
import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import com.mgmtp.internship_vacation_booking.model.YearlyQuotaEntity;
import com.mgmtp.internship_vacation_booking.repository.EmployeeYearlyQuotaRepository;
import com.mgmtp.internship_vacation_booking.repository.RequestRepository;
import com.mgmtp.internship_vacation_booking.repository.YearlyQuotaRepository;
import com.mgmtp.internship_vacation_booking.service.CustomEmpDetailsService;
import com.mgmtp.internship_vacation_booking.service.request_status.RequestStatusService;
import com.mgmtp.internship_vacation_booking.service.vacation_type.VacationTypeService;
import com.mgmtp.internship_vacation_booking.util.DateTimeUtil;
import com.mgmtp.internship_vacation_booking.util.JodaTimeService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class YearlyQuotaServiceImpl implements YearlyQuotaService {

    private final YearlyQuotaRepository yearlyQuotaRepository;
    private final RequestRepository requestRepository;
    private final VacationTypeService vacationTypeService;
    private final RequestStatusService requestStatusService;
    private final CustomEmpDetailsService customEmpDetailsService;
    private final EmployeeYearlyQuotaRepository employeeYearlyQuotaRepository;

    @Autowired
    public YearlyQuotaServiceImpl(YearlyQuotaRepository yearlyQuotaRepository, RequestRepository requestRepository, VacationTypeService vacationTypeService, RequestStatusService requestStatusService, CustomEmpDetailsService customEmpDetailsService, EmployeeYearlyQuotaRepository employeeYearlyQuotaRepository) {
        this.yearlyQuotaRepository = yearlyQuotaRepository;
        this.requestRepository = requestRepository;
        this.vacationTypeService = vacationTypeService;
        this.requestStatusService = requestStatusService;
        this.customEmpDetailsService = customEmpDetailsService;
        this.employeeYearlyQuotaRepository = employeeYearlyQuotaRepository;
    }

    @Override
    public Integer getYearlyQuotaByVacationTypeAndYear(VacationTypeEntity vacationType, int year) {
        DateTime startWorkingDate = new DateTime(customEmpDetailsService.getCurrentUser().getStartWorkingDate());
        DateTime toDay = new DateTime(new Date());
        if (!vacationType.isDependOnStartWorkingDate() || startWorkingDate.getYear() < toDay.getYear()) {
            return yearlyQuotaRepository.findOneByVacationTypeAndYear(vacationType, year).getQuota();
        }
        DateTime lastDateInYear = new DateTime().dayOfYear().withMaximumValue().withTime(23, 59, 59, 999);
        return (Days.daysBetween(startWorkingDate, lastDateInYear).getDays() + 1) * yearlyQuotaRepository.findOneByVacationTypeAndYear(vacationType, year).getQuota() / 365;
    }

    private Integer getNewYearlyQuotaByVacationTypeAndYearOfEmployee(int employeeId, VacationTypeEntity vacationType, int year) {
        int yearlyQuotaId = yearlyQuotaRepository.findOneByVacationTypeAndYear(vacationType,year).getId();
        return employeeYearlyQuotaRepository.findOneByEmployeeAndYearlyQuota(employeeId,yearlyQuotaId).getNewQuota();
    }

    private boolean hasNewYearlyQuota(int employeeId,VacationTypeEntity vacationType, int year) {
        int yearlyQuotaId = yearlyQuotaRepository.findOneByVacationTypeAndYear(vacationType,year).getId();
        if(employeeYearlyQuotaRepository.findOneByEmployeeAndYearlyQuota(employeeId,yearlyQuotaId)!=null)
            return true;
        return false;
    }

    @Override
    public List<RemainingDayRow> getRemainingDayRows() {
        List<RemainingDayRow> listRemainingDayRow = new LinkedList<>();
        int year = Year.now().getValue();
        int employeeId = customEmpDetailsService.getCurrentUser().getId();
        for (VacationTypeEntity vacationType : vacationTypeService.getAllVacationType()) {
            RemainingDayRow remainingDayRow = new RemainingDayRow();
            remainingDayRow.setVacationType(vacationType.getKey());
            remainingDayRow.setYearlyQuota(getYearlyQuotaByVacationTypeAndYear(vacationType, year));
            if(hasNewYearlyQuota(employeeId,vacationType, year)){
                remainingDayRow.setYearlyQuota(getNewYearlyQuotaByVacationTypeAndYearOfEmployee(employeeId,vacationType, year));
                if (getNewYearlyQuotaByVacationTypeAndYearOfEmployee(employeeId,vacationType, year) != null) {
                    remainingDayRow.setRemainingDays(getNewYearlyQuotaByVacationTypeAndYearOfEmployee(employeeId,vacationType, year) - getTotalTimeOffByVacationTypeAndYear(employeeId,vacationType, year));
                } else {
                    remainingDayRow.setRemainingDays(getNewYearlyQuotaByVacationTypeAndYearOfEmployee(employeeId,vacationType, year));
                }
            } else {
                if (getYearlyQuotaByVacationTypeAndYear(vacationType, year) != null) {
                    remainingDayRow.setRemainingDays(getYearlyQuotaByVacationTypeAndYear(vacationType, year) - getTotalTimeOffByVacationTypeAndYear(employeeId,vacationType, year));
                } else {
                    remainingDayRow.setRemainingDays(getYearlyQuotaByVacationTypeAndYear(vacationType, year));
                }
            }
            listRemainingDayRow.add(remainingDayRow);
        }
        return listRemainingDayRow;
    }

    @Override
    public int getTotalTimeOffByVacationTypeAndYear(int employeeId, VacationTypeEntity vacationType, int year) {
        int sum = 0;
        for (RequestEntity request : requestRepository.findEmployeeRequestsByVacationTypeAndYear(employeeId, vacationType.getId(), year)) {
            if (requestStatusService.isApprovalRequest(request)) {
                sum = sum + JodaTimeService.getWorkDaysFromTwoDates(request.getFromDate(), request.getToDate());
            }
        }
        return sum;
    }

    @Override
    public Integer getRemainDays(VacationTypeEntity vacationType,int employeeId) {
        int currentYear = DateTimeUtil.getCurrentYear();
        if (hasNewYearlyQuota(employeeId,vacationType, currentYear)) {
            if (getNewYearlyQuotaByVacationTypeAndYearOfEmployee(employeeId,vacationType, currentYear) != null)
                return getNewYearlyQuotaByVacationTypeAndYearOfEmployee(employeeId,vacationType, currentYear) - getTotalTimeOffByVacationTypeAndYear(employeeId,vacationType, currentYear);
        } else {
            if (getYearlyQuotaByVacationTypeAndYear(vacationType, currentYear) != null)
                return getYearlyQuotaByVacationTypeAndYear(vacationType, currentYear) - getTotalTimeOffByVacationTypeAndYear(employeeId,vacationType, currentYear);
        }
        return null;
    }

    @Override
    public List<TimeOffDays> getTimeOffDaysByYear(int year) {
        List<YearlyQuotaEntity> yearlyQuotaEntities = yearlyQuotaRepository.findByYearOrderByVacationTypeDisplayOrderAsc(year);
        List<TimeOffDays> timeOffDaysList = new ArrayList<>();
        for (YearlyQuotaEntity yearlyQuotaEntity : yearlyQuotaEntities) {
            TimeOffDays timeOffDays = new TimeOffDays();
            timeOffDays.setYearlyQuotaId(yearlyQuotaEntity.getId());
            timeOffDays.setVacationName(yearlyQuotaEntity.getVacationType().getKey());
            Integer quota = yearlyQuotaEntity.getQuota();
            timeOffDays.setQuota(quota != null ? Integer.toString(quota) : "");
            timeOffDaysList.add(timeOffDays);
        }
        return timeOffDaysList;
    }

    @Override
    public List<YearlyQuotaEntity> copyQuotaData(int selectedYear) {
        List<YearlyQuotaEntity> yearlyQuotaEntities = new ArrayList<>();
        for (YearlyQuotaEntity yearlyQuotaEntity : yearlyQuotaRepository.findByYear(selectedYear - 1)) {
            YearlyQuotaEntity newYearlyQuota = new YearlyQuotaEntity();
            newYearlyQuota.setYear(selectedYear);
            newYearlyQuota.setQuota(yearlyQuotaEntity.getQuota());
            newYearlyQuota.setVacationType(yearlyQuotaEntity.getVacationType());
            yearlyQuotaRepository.save(newYearlyQuota);
            yearlyQuotaEntities.add(newYearlyQuota);
        }
        return yearlyQuotaEntities;
    }

}
