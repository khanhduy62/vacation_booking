package com.mgmtp.internship_vacation_booking.service.super_admin;

import com.mgmtp.internship_vacation_booking.dto.SuperAdminRequest;
import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import com.mgmtp.internship_vacation_booking.service.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public void saveApprover(SuperAdminRequest superAdminRequest) {
        Set<EmployeeEntity> employeeEntities = new HashSet<>();

        EmployeeEntity employee = employeeService.findEmployeeById(superAdminRequest.getEmployeeId());

        for (Integer approverId: superAdminRequest.getApproverList()) {
            employeeEntities.add(employeeService.findEmployeeById(approverId));
        }

        employee.setApprovers(employeeEntities);
        employeeService.save(employee);
    }
}
