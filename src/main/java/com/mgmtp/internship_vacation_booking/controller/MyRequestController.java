package com.mgmtp.internship_vacation_booking.controller;

import com.mgmtp.internship_vacation_booking.dto.EmployeeHistoryRequest;
import com.mgmtp.internship_vacation_booking.dto.RequestForm;
import com.mgmtp.internship_vacation_booking.service.employee.EmployeeService;
import com.mgmtp.internship_vacation_booking.service.request.RequestService;
import com.mgmtp.internship_vacation_booking.service.request_status.RequestStatusService;
import com.mgmtp.internship_vacation_booking.service.vacationtype.VacationTypeService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

/**
 * Created by ASUS on 12/12/2016.
 */
@Controller
@RequestMapping("/my-request")
@Log
public class MyRequestController {
    private final VacationTypeService vacationTypeService;
    private final RequestService requestService;
    private final EmployeeService employeeService;
    private final RequestStatusService requestStatusService;
    private final MessageSource messageSource;

    @Autowired
    public MyRequestController(VacationTypeService vacationTypeService, RequestService requestService, RequestStatusService requestStatusService, MessageSource messageSource, EmployeeService employeeService) {
        this.vacationTypeService = vacationTypeService;
        this.requestService = requestService;
        this.employeeService = employeeService;
        this.requestStatusService = requestStatusService;
        this.messageSource = messageSource;
    }

    @RequestMapping("/booking")
    public String getMyRequest(Model model, Principal principal) {
        model.addAttribute("requestForm", new RequestForm());
        model.addAttribute("listVacationType", vacationTypeService.getAllVacationType());
        model.addAttribute("approverNames", employeeService.findApproverNamesByEmployeeEmail(principal.getName()));
        return "myrequest/booking";
    }

    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    public String saveRequest(@Valid @ModelAttribute("requestForm") RequestForm requestForm, BindingResult bindingResult, Model model, Locale locale, RedirectAttributes redirectAttributes) {
        log.info("requestForm with information: " + requestForm.toString());
        if (bindingResult.hasErrors()) {
            model.addAttribute("listVacationType", vacationTypeService.getAllVacationType());
            return "myrequest/booking";
        }
        redirectAttributes.addFlashAttribute("bookingSuccessMessage", messageSource.getMessage("booking.success.message", null, locale) );
        requestService.saveRequest(requestForm);

        return "redirect:/my-request/booking";
    }

    @RequestMapping("/history")
    public String getHistory(Principal principal, Model model) {
        String email = principal.getName();
        List<EmployeeHistoryRequest> employeeHistoryRequests = requestStatusService.getEmployeeHistoriesByEmail(email);
        model.addAttribute("employeeHistoryRequests", employeeHistoryRequests);
        return "myrequest/myrequest_history";
    }
}
