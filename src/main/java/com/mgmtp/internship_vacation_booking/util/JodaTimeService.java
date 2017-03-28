package com.mgmtp.internship_vacation_booking.util;

import com.mgmtp.internship_vacation_booking.dto.YearlyPublicHoliday;
import com.mgmtp.internship_vacation_booking.service.public_holiday.PublicHolidayService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JodaTimeService{

    private static PublicHolidayService publicHolidayService;

    @Autowired
    public JodaTimeService(PublicHolidayService publicHolidayService) {
        JodaTimeService.publicHolidayService = publicHolidayService;
    }

    public static int getWorkDaysFromTwoDates(Date from, Date to) {
        DateTime dt1 = new DateTime(from);
        DateTime dt2 = new DateTime(to);
        int days = 0;
        while(dt1.compareTo(dt2) <= 0 ){
            if(dt1.getDayOfWeek() !=  DateTimeConstants.SATURDAY && dt1.getDayOfWeek() !=  DateTimeConstants.SUNDAY){
                days = days + 1;
                for (YearlyPublicHoliday yearlyPublicHoliday : publicHolidayService.getCurrentYearPublicHolidays()) {
                    if(isWithinRange(dt1, yearlyPublicHoliday.getFromDate(), yearlyPublicHoliday.getToDate())) {
                        days = days -1;
                    }
                }
            }
            dt1 = dt1.plusDays(1);
        }
        return days;
    }

    public static boolean isWithinRange(DateTime testDate, Date startDate, Date endDate) {
        DateTime start =  new DateTime(startDate).withTimeAtStartOfDay();
        DateTime end =  new DateTime(endDate).withTime(23, 59, 59, 999);
        Interval interval = new Interval(start, end);
        return interval.contains(testDate);
    }
}
