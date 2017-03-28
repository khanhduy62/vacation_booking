package com.mgmtp.internship_vacation_booking.service.public_holiday;

import com.mgmtp.internship_vacation_booking.dto.YearlyPublicHoliday;
import com.mgmtp.internship_vacation_booking.model.YearlyPublicHolidayEntity;
import com.mgmtp.internship_vacation_booking.repository.YearlyPublicHolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class PublicHolidayServiceImpl implements PublicHolidayService {

    private final YearlyPublicHolidayRepository yearlyPublicHolidayRepository;

    @Autowired
    public PublicHolidayServiceImpl(YearlyPublicHolidayRepository yearlyPublicHolidayRepository) {
        this.yearlyPublicHolidayRepository = yearlyPublicHolidayRepository;
    }

    @Override
    public List<YearlyPublicHoliday> getYearlyPublicHolidayByYear(int year) {
        List<YearlyPublicHoliday> yearlyPublicHolidays = new ArrayList<>();
        List<YearlyPublicHolidayEntity> yearlyPublicHolidayEntities = yearlyPublicHolidayRepository.findByYearOrderByFromDateAsc(year);
        for (YearlyPublicHolidayEntity yearlyPublicHolidayEntity : yearlyPublicHolidayEntities) {
            YearlyPublicHoliday yearlyPublicHoliday = new YearlyPublicHoliday();
            yearlyPublicHoliday.setName(yearlyPublicHolidayEntity.getPublicHoliday().getName());
            yearlyPublicHoliday.setDescription(yearlyPublicHolidayEntity.getPublicHoliday().getDescription());
            yearlyPublicHoliday.setKey(yearlyPublicHolidayEntity.getPublicHoliday().getKey());
            yearlyPublicHoliday.setFromDate(yearlyPublicHolidayEntity.getFromDate());
            yearlyPublicHoliday.setToDate(yearlyPublicHolidayEntity.getToDate());
            yearlyPublicHolidays.add(yearlyPublicHoliday);
        }
        return yearlyPublicHolidays;
    }

    @Override
    public List<YearlyPublicHoliday> getCurrentYearPublicHolidays() {
        return getYearlyPublicHolidayByYear(Year.now().getValue());
    }

    @Override
    public void copyData(int selectedYear) {
        for (YearlyPublicHolidayEntity entity : yearlyPublicHolidayRepository.findAllByYear(selectedYear - 1)) {
            YearlyPublicHolidayEntity newEntity = new YearlyPublicHolidayEntity();
            newEntity.setFromDate(plusOneYear(entity.getFromDate()));
            newEntity.setToDate(plusOneYear(entity.getToDate()));
            newEntity.setPublicHoliday(entity.getPublicHoliday());
            yearlyPublicHolidayRepository.save(newEntity);
        }
    }

    public Date plusOneYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        return new Date(calendar.getTimeInMillis());
    }
}
