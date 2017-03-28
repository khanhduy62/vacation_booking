package com.mgmtp.internship_vacation_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Jango on 12/15/2016.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = "/pending")
    public String pending() {
        return "admin/pending_request";
    }
}
