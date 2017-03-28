package com.mgmtp.internship_vacation_booking.repository;

import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacationTypeRepository extends JpaRepository<VacationTypeEntity, Integer> {

    List<VacationTypeEntity> findAllByOrderByDisplayOrderAsc();

}

