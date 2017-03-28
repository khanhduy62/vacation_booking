package com.mgmtp.internship_vacation_booking.repository;

import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    EmployeeEntity findByEmail(String email);

    EmployeeEntity findById(int employeeId);

    @Query("select employee from EmployeeEntity employee where UPPER(CONCAT(employee.firstName,' ',employee.lastName)) like UPPER(?1)")
    List<EmployeeEntity> findByFullName(String fullName);
}
