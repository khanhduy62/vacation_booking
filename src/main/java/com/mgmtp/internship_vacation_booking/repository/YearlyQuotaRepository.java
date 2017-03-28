package com.mgmtp.internship_vacation_booking.repository;

import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import com.mgmtp.internship_vacation_booking.model.YearlyQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableTransactionManagement
public interface YearlyQuotaRepository extends JpaRepository<YearlyQuotaEntity, Integer> {

    YearlyQuotaEntity findOneByVacationTypeAndYear(VacationTypeEntity vacationType, int year);

    List<YearlyQuotaEntity> findByYear(int year);

    List<YearlyQuotaEntity> findByYearOrderByVacationTypeDisplayOrderAsc(int year);

    @Modifying
    @Transactional
    @Query("update YearlyQuotaEntity set quota=:quota where id=:id")
    void updateQuotaById(@Param(value = "id") int id, @Param(value = "quota") Integer quota);
}
