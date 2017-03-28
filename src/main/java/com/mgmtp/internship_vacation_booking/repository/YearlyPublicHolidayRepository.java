package com.mgmtp.internship_vacation_booking.repository;

import com.mgmtp.internship_vacation_booking.model.YearlyPublicHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YearlyPublicHolidayRepository extends JpaRepository<YearlyPublicHolidayEntity, Integer> {

    @Query(value = "select p from YearlyPublicHolidayEntity p where YEAR(p.fromDate)=:year or YEAR(p.toDate)=:year order by p.fromDate asc")
    List<YearlyPublicHolidayEntity> findByYearOrderByFromDateAsc(@Param("year") int year);

    @Query("select v from YearlyPublicHolidayEntity v where year(v.fromDate)=:year or year(v.toDate)=:year")
    List<YearlyPublicHolidayEntity> findAllByYear(@Param("year") int year);
}
