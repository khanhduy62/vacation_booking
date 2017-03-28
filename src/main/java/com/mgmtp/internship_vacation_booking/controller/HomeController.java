package com.mgmtp.internship_vacation_booking.controller;

import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import com.mgmtp.internship_vacation_booking.model.RoleEntity;
import com.mgmtp.internship_vacation_booking.repository.EmployeeRepository;
import com.mgmtp.internship_vacation_booking.repository.RoleRepository;
import com.mgmtp.internship_vacation_booking.service.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
class HomeController {

    @Autowired
    EmployeeService employeeService;

    @RequestMapping("/")
    String index(Principal principal, ModelMap model) {
        String email = principal.getName();
        EmployeeEntity emp = this.employeeService.findByMail(email);
        model.addAttribute("emp", emp);
        for (RoleEntity role : emp.getRoles()) {
            String r = role.getName();
            if ("ROLE_ADMIN".equals(r)) {
                return "redirect:leader/listPendingRequest";
            } else if ("ROLE_USER".equals(r)) {
                return "redirect:my-request/booking";
            } else if ("ROLE_SUPER_ADMIN".equals(r)) {
                return "redirect:superadmin/manage_approvers";
            }
        }
        return "users/403";
    }

}
