package com.mgmtp.internship_vacation_booking.repository;

import com.mgmtp.internship_vacation_booking.model.PublicHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHolidayEntity, Integer> {
}
