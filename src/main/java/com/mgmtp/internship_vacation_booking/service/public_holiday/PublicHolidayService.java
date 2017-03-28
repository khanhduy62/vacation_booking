package com.mgmtp.internship_vacation_booking.service.public_holiday;

import com.mgmtp.internship_vacation_booking.dto.YearlyPublicHoliday;

import java.util.List;

public interface PublicHolidayService {
    List<YearlyPublicHoliday> getYearlyPublicHolidayByYear(int year);
    List<YearlyPublicHoliday> getCurrentYearPublicHolidays();
    void copyData(int selectedYear);
}
