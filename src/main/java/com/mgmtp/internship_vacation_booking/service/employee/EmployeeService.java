package com.mgmtp.internship_vacation_booking.service.employee;

import com.mgmtp.internship_vacation_booking.dto.AjaxResponseErrorField;
import com.mgmtp.internship_vacation_booking.dto.Employee;
import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import com.mgmtp.internship_vacation_booking.model.RoleEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface EmployeeService {

    EmployeeEntity findByMail(String mail);

    List<EmployeeEntity> findListApprovers(int employeeId);

    List<Employee> getApproverByEmployeeId(int employeeId);

    List<String> findApproverNamesByEmployeeEmail(String employeeEmail);

    Set<RoleEntity> getRolesByEmployeeId(int employeeId);

    EmployeeEntity findEmployeeById(int employeeId);

    void save(EmployeeEntity employee);

    List<Employee> getByFullName(String fullName);

    List<Employee> getEmployeesByRoleName(String roleName);

    AjaxResponseErrorField convertBindingResultToAjaxResponseErrorField(BindingResult bindingResult, Locale locale);
}
