package com.mgmtp.internship_vacation_booking.validation.validator;

import com.mgmtp.internship_vacation_booking.dto.SuperAdminRequest;
import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import com.mgmtp.internship_vacation_booking.model.RoleEntity;
import com.mgmtp.internship_vacation_booking.service.employee.EmployeeService;
import com.mgmtp.internship_vacation_booking.validation.annotation.ValidSuperAdminRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class ValidSuperAdminRequestValidator implements ConstraintValidator<ValidSuperAdminRequest, SuperAdminRequest> {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public void initialize(ValidSuperAdminRequest superAdmin) {

    }

    @Override
    public boolean isValid(SuperAdminRequest superAdminRequest, ConstraintValidatorContext context) {
        if (superAdminRequest.getEmployeeId() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{ValidSuperAdminRequestValidator.employeeName}")
                    .addPropertyNode("employeeId").addConstraintViolation();
            return false;
        }
        EmployeeEntity employee = employeeService.findEmployeeById(superAdminRequest.getEmployeeId());
        if (employee == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{ValidSuperAdminRequestValidator.employeeName}")
                    .addPropertyNode("employeeId").addConstraintViolation();
            return false;
        }

        int order = 1;
        for (Integer approverId : superAdminRequest.getApproverList()) {
            if (!checkValidApprover((approverId), context, order))
                return false;
            order++;
        }

        return true;
    }

    @Transactional
    private boolean checkValidApprover(Integer approverId, ConstraintValidatorContext context, int approverOrder) {
        if (approverId == null || (employeeService.findEmployeeById(approverId)) == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("approver" + approverOrder)
                    .addPropertyNode("approverErrorField").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("{ValidSuperAdminRequestValidator.approver}")
                    .addPropertyNode("approverList").addConstraintViolation();
            return false;
        }
        if (!hasAdminRole(employeeService.getRolesByEmployeeId(approverId))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("approver" + approverOrder)
                    .addPropertyNode("approverErrorField").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("{ValidSuperAdminRequestValidator.approver.role}")
                    .addPropertyNode("approverList").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean hasAdminRole(Set<RoleEntity> roles) {
        for (RoleEntity role : roles)
            if (role.getName().equalsIgnoreCase("ROLE_ADMIN"))
                return true;
        return false;
    }

}

