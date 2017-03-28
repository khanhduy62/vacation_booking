package com.mgmtp.internship_vacation_booking.controller;

import com.mgmtp.internship_vacation_booking.dto.AdminPendingRequest;
import com.mgmtp.internship_vacation_booking.dto.AjaxResponseBody;
import com.mgmtp.internship_vacation_booking.dto.RequestStatusDetails;
import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import com.mgmtp.internship_vacation_booking.repository.EmployeeRepository;
import com.mgmtp.internship_vacation_booking.service.CustomEmpDetail;
import com.mgmtp.internship_vacation_booking.service.employee.EmployeeService;
import com.mgmtp.internship_vacation_booking.service.employee.LeaderService;
import com.mgmtp.internship_vacation_booking.service.request.RequestService;
import com.mgmtp.internship_vacation_booking.service.request_status.RequestStatusService;
import com.mgmtp.internship_vacation_booking.service.sort.SortBy;
import com.mgmtp.internship_vacation_booking.service.sort.SortDirection;
import com.mgmtp.internship_vacation_booking.service.sort.SortService;
import com.mgmtp.internship_vacation_booking.service.yearly_quota.YearlyQuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/leader")
public class LeaderController {
    private static final int MAX_PAGES_IN_PAGINATION = 10;
    private static final int DEFAULT_NUMBER_OF_ROWS_PER_PAGE = 20;

    private final EmployeeService employeeService;
    private final LeaderService leaderService;
    private final EmployeeRepository employeeRepository;
    private final RequestStatusService requestStatusService;
    private final MessageSource messageSource;
    private final YearlyQuotaService yearlyQuotaService;
    private final RequestService requestService;
    private final SortService sortService;

    @Autowired
    public LeaderController(EmployeeService employeeService,
                            LeaderService leaderService,
                            EmployeeRepository employeeRepository,
                            RequestStatusService requestStatusService,
                            MessageSource messageSource,
                            YearlyQuotaService yearlyQuotaService,
                            RequestService requestService,
                            SortService sortService) {
        this.employeeService = employeeService;
        this.leaderService = leaderService;
        this.employeeRepository = employeeRepository;
        this.requestStatusService = requestStatusService;
        this.messageSource = messageSource;
        this.yearlyQuotaService = yearlyQuotaService;
        this.requestService = requestService;
        this.sortService = sortService;
    }

    /**
     * Get pending requests of an admin
     *
     * @param pageNumber   Current page number
     * @param sizeAsString Number of rows in 1 page, sizeAsString = 'all' means all of the rows in the database
     * @return view string
     */
    @RequestMapping("/listPendingRequest")
    public String listPendingRequest(@RequestParam(name = "page", defaultValue = "1") Integer pageNumber,
                                     @RequestParam(name = "size", defaultValue = "20") String sizeAsString,
                                     @RequestParam(name = "sort", defaultValue = "") String sortBy,
                                     @RequestParam(name = "direction", defaultValue = "") String sortDirection,
                                     @RequestParam(name = "search", defaultValue = "") String searchKeyWord,
                                     Model model) {
        if (pageNumber < 1) return "redirect:/leader/listPendingRequest";

        // validate size's value
        int size;
        if ("all".equals(sizeAsString)) {
            size = Integer.MAX_VALUE;
        } else {
            try {
                size = Integer.parseInt(sizeAsString);
                if (size <= 0) size = DEFAULT_NUMBER_OF_ROWS_PER_PAGE;
            } catch (NumberFormatException e) {
                size = DEFAULT_NUMBER_OF_ROWS_PER_PAGE;
            }
        }

        SortBy sortByEnum = sortService.getSortByEnum(sortBy);
        SortDirection sortDirectionEnum = sortService.getSortDirectionEnum(sortDirection);

        CustomEmpDetail customEmpDetail = (CustomEmpDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EmployeeEntity emp = employeeService.findByMail(customEmpDetail.getUsername());

        Page<AdminPendingRequest> adminPendingRequestPage;
        if ("".equals(searchKeyWord)) {
            adminPendingRequestPage = leaderService.findPendingRequestByLeaderId(emp.getId(), pageNumber, size, sortByEnum, sortDirectionEnum);
        } else {
            adminPendingRequestPage = leaderService.findPendingRequestByLeaderId(emp.getId(), searchKeyWord.trim().toLowerCase(), pageNumber, size, sortByEnum, sortDirectionEnum);
            if (adminPendingRequestPage.getTotalElements() == 0) {
                model.addAttribute("searchNotFound", messageSource.getMessage("search.not.found", new Object[]{searchKeyWord}, null));
            }
        }

        if (pageNumber > adminPendingRequestPage.getTotalPages() && adminPendingRequestPage.getTotalPages() > 0)
            return "redirect:/leader/listPendingRequest";
        model.addAttribute("listAdminPendingRequest", adminPendingRequestPage.getContent());
        model.addAttribute("currentUser", emp);
        model.addAttribute("size", size == Integer.MAX_VALUE ? "all" : size);
        model.addAttribute("sortBy", sortByEnum);
        model.addAttribute("sortDirection", sortDirectionEnum);
        model.addAttribute("search", searchKeyWord);
        setPaginationInfo(model, adminPendingRequestPage);
        return "admin/pending_request";
    }

    private void setPaginationInfo(Model model, Page page) {
        int currentIndex = page.getNumber() + 1;
        int beginIndex = 1, endIndex = page.getTotalPages();
        if (page.getTotalPages() > MAX_PAGES_IN_PAGINATION) {
            beginIndex = Math.max(1, currentIndex - MAX_PAGES_IN_PAGINATION / 2);
            endIndex = Math.min(beginIndex + MAX_PAGES_IN_PAGINATION - 1, page.getTotalPages());
            if (endIndex - beginIndex + 1 < MAX_PAGES_IN_PAGINATION) {
                beginIndex -= MAX_PAGES_IN_PAGINATION - (endIndex - beginIndex + 1);
            }
        }

        model.addAttribute("beginIndex", beginIndex);
        model.addAttribute("endIndex", endIndex);
        model.addAttribute("currentIndex", currentIndex);
    }

    @ResponseBody
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public AjaxResponseBody changeStatus(Principal principal,
                                         @Valid @ModelAttribute("requestStatusDetails") RequestStatusDetails requestStatusDetails,
                                         BindingResult bindingResult) {
        int errorCode = 0; // default is updated successfully
        int leaderId = employeeRepository.findByEmail(principal.getName()).getId();
        StringBuilder stringBuilder = new StringBuilder();

        if (!bindingResult.hasErrors()) {
            requestStatusDetails.setLeaderId(leaderId);
            boolean isSuccessful = requestStatusService.updateRequestStatus(requestStatusDetails);

            if (!isSuccessful) {
                errorCode = AjaxResponseBody.ERROR_UPDATE_FAILURE;
                stringBuilder.append(messageSource.getMessage("runtimeError.requestStatus.updateRequestStatusDetails", null, null));
            }
        } else {
            errorCode = AjaxResponseBody.ERROR_INVALID_DAYS;
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError error : errors) {
                if ("ValidTimeOffDays".equals(error.getCode())) {
                    stringBuilder.append(getTimeOffDaysErrorMessage(requestStatusDetails.getRequestId()));
                } else {
                    stringBuilder.append(error.getDefaultMessage());
                    stringBuilder.append(System.getProperty("line.separator"));
                }
            }
        }

        AjaxResponseBody result = new AjaxResponseBody();
        result.setErrorCode(errorCode);
        result.setMessage(stringBuilder.toString());
        return result;
    }

    private String getTimeOffDaysErrorMessage(int requestId) {
        VacationTypeEntity vacationType = requestService.getVacationTypeByRequestId(requestId);
        Object remainDays = yearlyQuotaService.getRemainDays(vacationType,requestService.getRequestById(requestId).getEmployeeByEmployeeId().getId());
        String vacationTypeName = vacationType.getName();
        return messageSource.getMessage("invalid.requestStatus.timeOffDays", new Object[]{remainDays, vacationTypeName}, null);
    }
}
