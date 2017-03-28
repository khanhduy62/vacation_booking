package com.mgmtp.internship_vacation_booking.service;

import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import com.mgmtp.internship_vacation_booking.repository.EmployeeRepository;
import com.mgmtp.internship_vacation_booking.repository.RoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jango on 12/13/2016.
 */
@Service("customEmpDetailService")
public class CustomEmpDetailService implements UserDetailsService {

    private final EmployeeRepository empRepository;
    private final RoleRepository roleRepository;

    public CustomEmpDetailService(EmployeeRepository empRepository,RoleRepository roleRepository) {
        this.empRepository = empRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmployeeEntity emp = empRepository.findByEmail(username);
        if(null == emp){
            throw new UsernameNotFoundException("No User present with this email:"+username);
        }else {
            List<String> empRoles = roleRepository.findRoleByEmail(username);
            return new CustomEmpDetail(emp,empRoles);
        }
    }
}
