package com.mgmtp.internship_vacation_booking.repository;

import com.mgmtp.internship_vacation_booking.model.RequestStatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@EnableTransactionManagement
public interface RequestStatusRepository extends JpaRepository<RequestStatusEntity, Integer> {

    @Query("SELECT a FROM  RequestStatusEntity a WHERE a.requestStatusEntityPK.employeeByLeaderId.id = :leaderID")
    List<RequestStatusEntity> findByLeaderId(@Param("leaderID") int leaderID);

    @Query("SELECT a FROM  RequestStatusEntity a WHERE a.requestStatusEntityPK.requestByRequestId.id = :requestId")
    List<RequestStatusEntity> findByRequestId(@Param("requestId")int requestId);

    @Query("SELECT a FROM  RequestStatusEntity a WHERE a.requestStatusEntityPK.employeeByLeaderId.id = :leaderID")
    Page<RequestStatusEntity> findByLeaderIdPage(@Param("leaderID") int leaderID, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update RequestStatusEntity set approved=:approved " +
            "where requestStatusEntityPK.requestByRequestId.id=:requestId " +
            "and requestStatusEntityPK.employeeByLeaderId.id=:leaderId")
    void updateByRequestIdAndLeaderId(@Param(value = "requestId") int requestId,
                                      @Param(value = "leaderId") int leaderId,
                                      @Param(value = "approved") boolean approved);

    @Query("SELECT a FROM RequestStatusEntity a WHERE a.requestStatusEntityPK.employeeByLeaderId.id = :leaderId AND LOWER(CONCAT(a.requestStatusEntityPK.requestByRequestId.employeeByEmployeeId.firstName, ' ', a.requestStatusEntityPK.requestByRequestId.employeeByEmployeeId.lastName) ) LIKE CONCAT('%', :fullName,'%')")
    Page<RequestStatusEntity> findByLeaderIdAndFilterByFirstName(@Param("leaderId") int leaderId, @Param("fullName") String fullName, Pageable pageable);
}
