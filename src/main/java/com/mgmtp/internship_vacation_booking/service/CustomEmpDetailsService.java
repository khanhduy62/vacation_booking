package com.mgmtp.internship_vacation_booking.service;


import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomEmpDetailsService extends UserDetailsService {
    EmployeeEntity getCurrentUser();
}
