package com.mgmtp.internship_vacation_booking.repository;

import com.mgmtp.internship_vacation_booking.model.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {
    List<RequestEntity> findAll();

    @Query("SELECT a FROM  RequestEntity a WHERE a.employeeByEmployeeId.id = :employeeID AND a.vacationType.id = :vacationTypeId AND YEAR(a.fromDate) = :year")
    List<RequestEntity> findEmployeeRequestsByVacationTypeAndYear(@Param("employeeID") int employeeID, @Param("vacationTypeId") int vacationTypeId, @Param("year") int year);

    @Query("SELECT a FROM  RequestEntity a WHERE a.employeeByEmployeeId.id = :employeeID")
    List<RequestEntity> findEmployeeByEmployeeId(@Param("employeeID") int employeeID);


}