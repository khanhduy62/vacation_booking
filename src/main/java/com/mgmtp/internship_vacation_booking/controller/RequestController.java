package com.mgmtp.internship_vacation_booking.controller;

import com.mgmtp.internship_vacation_booking.dto.EmployeeHistoryRequest;
import com.mgmtp.internship_vacation_booking.dto.RequestForm;
import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import com.mgmtp.internship_vacation_booking.service.CustomEmpDetailsService;
import com.mgmtp.internship_vacation_booking.service.public_holiday.PublicHolidayService;
import com.mgmtp.internship_vacation_booking.service.request.RequestService;
import com.mgmtp.internship_vacation_booking.service.request_status.RequestStatusService;
import com.mgmtp.internship_vacation_booking.service.vacation_type.VacationTypeService;
import com.mgmtp.internship_vacation_booking.service.yearly_quota.YearlyQuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.Locale;

@Controller
@RequestMapping("/my-request")
public class RequestController {

    private final VacationTypeService vacationTypeService;
    private final RequestService requestService;
    private final YearlyQuotaService yearlyQuotaService;
    private final RequestStatusService requestStatusService;
    private final MessageSource messageSource;
    private final CustomEmpDetailsService customEmpDetailsService;
    private final PublicHolidayService publicHolidayService;

    @Autowired
    public RequestController(VacationTypeService vacationTypeService, RequestService requestService, YearlyQuotaService yearlyQuotaService,
                             RequestStatusService requestStatusService, MessageSource messageSource, CustomEmpDetailsService customEmpDetailsService, PublicHolidayService publicHolidayService) {
        this.vacationTypeService = vacationTypeService;
        this.requestService = requestService;
        this.yearlyQuotaService = yearlyQuotaService;
        this.requestStatusService = requestStatusService;
        this.messageSource = messageSource;
        this.customEmpDetailsService = customEmpDetailsService;
        this.publicHolidayService = publicHolidayService;
    }

    @RequestMapping("/booking")
    public String getMyRequest(Model model) {
        EmployeeEntity employee = this.customEmpDetailsService.getCurrentUser();
        model.addAttribute("requestForm", new RequestForm());
        model.addAttribute("listVacationType", vacationTypeService.getAllVacationType());
        model.addAttribute("remainingDayRows", yearlyQuotaService.getRemainingDayRows());
        model.addAttribute("publicHolidays", publicHolidayService.getCurrentYearPublicHolidays());
        model.addAttribute("approvers", employee.getApprovers());
        return "myrequest/booking";
    }

    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    public String saveRequest(@Valid @ModelAttribute("requestForm") RequestForm requestForm, BindingResult bindingResult,
                              Model model, RedirectAttributes redirectAttributes, Locale locale) {
        EmployeeEntity employee = this.customEmpDetailsService.getCurrentUser();
        if (bindingResult.hasErrors()) {
            model.addAttribute("listVacationType", vacationTypeService.getAllVacationType());
            model.addAttribute("remainingDayRows", yearlyQuotaService.getRemainingDayRows());
            model.addAttribute("approvers", employee.getApprovers());

            return "myrequest/booking";
        }
        redirectAttributes.addFlashAttribute("bookingSuccessMessage", messageSource.getMessage("booking.success.message", null, locale) );
        requestService.saveRequest(requestForm);

        return "redirect:/my-request/booking";
    }

    @ResponseBody
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public void changeCancelled(@RequestBody EmployeeHistoryRequest employeeHistoryRequest) {
        int id = employeeHistoryRequest.getRequestId();
        requestService.updateCancelled(id);
    }

    @RequestMapping("/history")
    public String getHistory(Model model) {
        model.addAttribute("employeeHistoryRequests", requestStatusService.getEmployeeHistories());
        return "myrequest/history";
    }

    @ResponseBody
    @RequestMapping(value = "/working-days", produces = "application/json; charset=utf-8")
    public String getWorkingDays(@RequestParam(name = "fromDate") Date fromDate, @RequestParam(name = "endDate") Date endDate, Locale locale) {
        return requestService.getDurationMessage(fromDate, endDate, locale);
    }
}
