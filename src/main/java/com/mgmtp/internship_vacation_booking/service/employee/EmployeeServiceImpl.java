package com.mgmtp.internship_vacation_booking.service.employee;

import com.mgmtp.internship_vacation_booking.dto.AjaxResponseErrorField;
import com.mgmtp.internship_vacation_booking.dto.Employee;
import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import com.mgmtp.internship_vacation_booking.model.RoleEntity;
import com.mgmtp.internship_vacation_booking.repository.EmployeeRepository;
import com.mgmtp.internship_vacation_booking.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository, MessageSource messageSource) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.messageSource = messageSource;
    }

    @Transactional
    @Override
    public EmployeeEntity findByMail(String mail) {
        EmployeeEntity employee = employeeRepository.findByEmail(mail);
        employee.getRoles().size();
        return employee;
    }

    @Override
    public List<EmployeeEntity> findListApprovers(int employeeId) {
        List<EmployeeEntity> listLeader = new ArrayList<>();
        EmployeeEntity leader = employeeRepository.findOne(employeeId).getLeader();
        while (leader != null) {
            listLeader.add(leader);
            if (leader.getLeader() != null) {
                leader = employeeRepository.findOne(leader.getLeader().getId());
            } else {
                leader = null;
            }
        }
        return listLeader;
    }

    @Override
    public List<String> findApproverNamesByEmployeeEmail(String employeeEmail) {
        EmployeeEntity employeeEntity = findByMail(employeeEmail);
        List<EmployeeEntity> approvers = findListApprovers(employeeEntity.getId());
        List<String> approverNames = new ArrayList<>();
        if (approvers.size() == 0) {
            approverNames.add("NONE");
        } else {
            for (EmployeeEntity e : approvers) {
                approverNames.add(e.getFullName());
            }
        }
        return approverNames;
    }

    @Transactional
    @Override
    public Set<RoleEntity> getRolesByEmployeeId(int employeeId) {
        EmployeeEntity employeeEntity = employeeRepository.findOne(employeeId);
        employeeEntity.getRoles().size();
        return employeeEntity.getRoles();
    }

    @Override
    public EmployeeEntity findEmployeeById(int employeeId) {
        return employeeRepository.findOne(employeeId);
    }

    @Override
    public void save(EmployeeEntity employee) {
        employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getByFullName(String fullName) {
        List<EmployeeEntity> employeeEntities = employeeRepository.findByFullName(fullName);
        List<Employee> employees = new ArrayList<>();
        if (employeeEntities.size() > 0)
            for (EmployeeEntity employeeEntity : employeeEntities)
                employees.add(new Employee(employeeEntity.getId(),
                        String.format("%s - %s", employeeEntity.getFullName(), employeeEntity.getEmail()),
                        employeeEntity.getFullName()));
        return employees;
    }

    @Transactional
    @Override
    public List<Employee> getEmployeesByRoleName(String roleName) {
        List<Employee> employees = new ArrayList<>();
        RoleEntity role = roleRepository.findRoleByName(roleName);
        role.getEmployees().size();
        Set<EmployeeEntity> employeeEntities = role.getEmployees();
        for (EmployeeEntity employeeEntity : employeeEntities)
            employees.add(new Employee(employeeEntity.getId(),
                    String.format("%s - %s", employeeEntity.getFullName(), employeeEntity.getEmail()),
                    employeeEntity.getId() + ""));
        return employees;
    }

    @Override
    public AjaxResponseErrorField convertBindingResultToAjaxResponseErrorField(BindingResult bindingResult, Locale locale) {
        int errorCode = 0; // default is updated successfully
        AjaxResponseErrorField ajaxResponseErrorField = new AjaxResponseErrorField();
        List<AjaxResponseErrorField.ErrorDescription>  errorDescriptions = new ArrayList<>();
        ajaxResponseErrorField.setErrorDescriptions(errorDescriptions);
        if (bindingResult.hasErrors()) {
            errorCode = AjaxResponseErrorField.INVALID_PARAMETERS;
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError error : errors) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    if (bindingResult.hasFieldErrors("employeeId")) {
                        AjaxResponseErrorField.ErrorDescription errorDescription = new AjaxResponseErrorField.ErrorDescription();
                        errorDescription.setFieldName(fieldError.getField());
                        errorDescription.setMessage(fieldError.getDefaultMessage());
                        errorDescriptions.add(errorDescription);
                    }
                }
            }
            if (bindingResult.hasFieldErrors("approverList")) {
                String approverErrorMessage = bindingResult.getFieldError("approverList").getDefaultMessage();
                String approverErrorField = bindingResult.getFieldError("approverErrorField").getDefaultMessage();
                AjaxResponseErrorField.ErrorDescription errorDescription = new AjaxResponseErrorField.ErrorDescription();
                errorDescription.setFieldName(approverErrorField);
                errorDescription.setMessage(approverErrorMessage);
                errorDescriptions.add(errorDescription);
            }
        } else {
            AjaxResponseErrorField.ErrorDescription errorDescription = new AjaxResponseErrorField.ErrorDescription();
            errorDescription.setFieldName("success");
            errorDescription.setMessage(messageSource.getMessage("update.approvers.success.message",null,locale));
            errorDescriptions.add(errorDescription);
        }
        ajaxResponseErrorField.setErrorCode(errorCode);
        return ajaxResponseErrorField;
    }

    @Transactional
    @Override
    public List<Employee> getApproverByEmployeeId(int employeeId) {
        EmployeeEntity employeeEntity = employeeRepository.findOne(employeeId);
        List<Employee> listApprover = new ArrayList<>();
        Set<EmployeeEntity> employeeEntities = employeeEntity.getApprovers();

        for (EmployeeEntity employee : employeeEntities)
            listApprover.add(new Employee(employee.getId(),
                    String.format("%s - %s", employee.getFullName(), employee.getEmail()),
                    employee.getId() + ""));
        return  listApprover;
    }

}
