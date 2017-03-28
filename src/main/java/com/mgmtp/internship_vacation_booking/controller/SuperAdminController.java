package com.mgmtp.internship_vacation_booking.controller;

import com.google.common.collect.Lists;
import com.mgmtp.internship_vacation_booking.dto.*;
import com.mgmtp.internship_vacation_booking.model.YearlyQuotaEntity;
import com.mgmtp.internship_vacation_booking.repository.YearlyQuotaRepository;
import com.mgmtp.internship_vacation_booking.service.employee.EmployeeService;
import com.mgmtp.internship_vacation_booking.service.public_holiday.PublicHolidayService;
import com.mgmtp.internship_vacation_booking.service.super_admin.SuperAdminService;
import com.mgmtp.internship_vacation_booking.service.yearly_quota.YearlyQuotaService;
import com.mgmtp.internship_vacation_booking.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminController {

    private final EmployeeService employeeService;
    private final SuperAdminService superAdminService;
    private final YearlyQuotaService yearlyQuotaService;
    private final MessageSource messageSource;
    private final YearlyQuotaRepository yearlyQuotaRepository;
    private final PublicHolidayService publicHolidayService;

    @Autowired
    public SuperAdminController(EmployeeService employeeService, SuperAdminService superAdminService, YearlyQuotaService yearlyQuotaService, MessageSource messageSource, YearlyQuotaRepository yearlyQuotaRepository, PublicHolidayService publicHolidayService) {
        this.employeeService = employeeService;
        this.superAdminService = superAdminService;
        this.yearlyQuotaService = yearlyQuotaService;
        this.messageSource = messageSource;
        this.yearlyQuotaRepository = yearlyQuotaRepository;
        this.publicHolidayService = publicHolidayService;
    }

    @RequestMapping("/manage_approvers")
    public String manageApprover(Model model) {
        model.addAttribute("superAdmin", new SuperAdminRequest());
        return "superadmin/manage_approvers";
    }

    @ResponseBody
    @RequestMapping(value = "/search_employees", method = RequestMethod.GET)
    public List<Employee> search(@RequestParam(name = "fullname", defaultValue = "") String fullName) {
        return employeeService.getByFullName("%" + fullName.trim() + "%");
    }

    @ResponseBody
    @RequestMapping(value = "/admin_list", method = RequestMethod.GET)
    public List<Employee> loadAdmin() {
        return employeeService.getEmployeesByRoleName("ROLE_ADMIN");
    }

    @ResponseBody
    @RequestMapping(value = "/valid_employee", method = RequestMethod.GET)
    public AjaxResponseEmployees getEmployee(@RequestParam(name = "fullName") String fullName, Locale locale) {
        AjaxResponseEmployees ajaxResponseEmployees = new AjaxResponseEmployees();
        ajaxResponseEmployees.setEmployeeList(employeeService.getByFullName(fullName.trim()));
        ajaxResponseEmployees.setErrorMessage(messageSource.getMessage("ValidSuperAdminRequestValidator.employeeName", null, locale));
        return ajaxResponseEmployees;
    }

    @ResponseBody
    @RequestMapping(value = "/approvers_of_employee", method = RequestMethod.GET)
    public List<Employee> loadApprover(@RequestParam(name = "employeeId") int employeeId) {
        return employeeService.getApproverByEmployeeId(employeeId);
    }

    @ResponseBody
    @RequestMapping(value = "/update_approvers", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public AjaxResponseErrorField saveApprover(@Valid @RequestBody SuperAdminRequest superAdminRequest, BindingResult bindingResult, Locale locale) {
        if (!bindingResult.hasErrors())
            superAdminService.saveApprover(superAdminRequest);
        return employeeService.convertBindingResultToAjaxResponseErrorField(bindingResult, locale);
    }

    @RequestMapping(value = "/manage_time_off_days", method = RequestMethod.GET)
    public String manageTimeOff(Model model, @RequestParam(name = "year", defaultValue = "0") Integer year) {
        int currentYear = DateTimeUtil.getCurrentYear();
        int chosenYear = (year == null || year == 0) ? currentYear : year;

        if (chosenYear < currentYear || chosenYear > currentYear + 1) {
            chosenYear = currentYear;
            model.addAttribute("invalidYearError",
                    messageSource.getMessage("ManageTimeOffDays.Year.Invalid", null, null));
        }

        model.addAttribute("listYears", Lists.newArrayList(currentYear, currentYear + 1));
        model.addAttribute("chosenYear", chosenYear);
        model.addAttribute("timeOffDaysList", yearlyQuotaService.getTimeOffDaysByYear(chosenYear));
        model.addAttribute("publicHolidays", publicHolidayService.getYearlyPublicHolidayByYear(chosenYear));

        return "superadmin/manage_time_off_days";
    }

    @ResponseBody
    @RequestMapping(value = "/manage_time_off_days", method = RequestMethod.POST)
    public AjaxResponseBody copyTimeOffDays(Model model,
                                            @RequestParam(name = "selectedYear") int selectedYear, Locale locale) {
        List<YearlyQuotaEntity> yearlyQuotaEntities = yearlyQuotaService.copyQuotaData(selectedYear);
        publicHolidayService.copyData(selectedYear);
        model.addAttribute("listYears", Lists.newArrayList(selectedYear - 1, selectedYear));
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("yearlyQuotaEntities", yearlyQuotaEntities);
        AjaxResponseBody result = new AjaxResponseBody();
        result.setErrorCode(0);
        result.setMessage(messageSource.getMessage("copyData.success.message", null, locale));
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updateQuotaValue", method = RequestMethod.POST)
    public AjaxResponseBody updateQuotaValue(
            @RequestParam(name = "yearlyQuotaId") Integer yearlyQuotaId,
            @RequestParam(name = "newValue") Integer newValue,
            Locale locale) {
        yearlyQuotaRepository.updateQuotaById(yearlyQuotaId, newValue);
        AjaxResponseBody result = new AjaxResponseBody();
        result.setErrorCode(0);
        result.setMessage(messageSource.getMessage("updateYearlyQuota.success.message", null, locale));
        return result;
    }

}
