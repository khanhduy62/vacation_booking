package com.mgmtp.internship_vacation_booking.util;

import java.time.Year;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static int getCurrentYear() {
        return Year.now().getValue();
    }

}
