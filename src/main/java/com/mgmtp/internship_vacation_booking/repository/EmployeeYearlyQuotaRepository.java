package com.mgmtp.internship_vacation_booking.repository;

import com.mgmtp.internship_vacation_booking.model.EmployeeYearlyQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeYearlyQuotaRepository extends JpaRepository<EmployeeYearlyQuotaEntity, Integer> {

    @Query("SELECT a FROM  EmployeeYearlyQuotaEntity a WHERE a.employeeYearlyQuotaEntityPK.employee.id = :employeeId " +
            "and a.employeeYearlyQuotaEntityPK.yearlyQuota.id = :yearlyQuotaId")
    EmployeeYearlyQuotaEntity findOneByEmployeeAndYearlyQuota(@Param("employeeId")int employeeId, @Param("yearlyQuotaId")int yearlyQuotaId);
}
