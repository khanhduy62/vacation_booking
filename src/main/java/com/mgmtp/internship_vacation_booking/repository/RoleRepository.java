package com.mgmtp.internship_vacation_booking.repository;

import com.mgmtp.internship_vacation_booking.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {
    RoleEntity findRoleByName(String roleName);
}
