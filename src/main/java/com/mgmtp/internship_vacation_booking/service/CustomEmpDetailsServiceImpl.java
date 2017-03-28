package com.mgmtp.internship_vacation_booking.service;


import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import com.mgmtp.internship_vacation_booking.model.RoleEntity;
import com.mgmtp.internship_vacation_booking.repository.EmployeeRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("customEmpDetailsService")
public class CustomEmpDetailsServiceImpl implements CustomEmpDetailsService {
    private final EmployeeRepository empRepository;

    public CustomEmpDetailsServiceImpl(EmployeeRepository empRepository) {
        this.empRepository = empRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmployeeEntity emp = empRepository.findByEmail(username);
        if (null == emp) {
            throw new UsernameNotFoundException("No User present with this email:" + username);
        } else {
            List<String> empRoles = new ArrayList<>();
            for (RoleEntity re : emp.getRoles()) {
                empRoles.add(re.getName());
            }
            return new CustomEmpDetail(emp, empRoles);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeEntity getCurrentUser() {
        CustomEmpDetail customEmpDetail = (CustomEmpDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EmployeeEntity currentEmployee = empRepository.findByEmail(customEmpDetail.getUsername());
        currentEmployee.getApprovers().size();
        return currentEmployee;
    }
}
