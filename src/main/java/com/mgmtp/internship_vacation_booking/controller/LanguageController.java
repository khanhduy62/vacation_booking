package com.mgmtp.internship_vacation_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LanguageController {
    @RequestMapping("/changeLanguage")
    String changeLanguage(@RequestParam(name = "returnUrl") String returnUrl, @RequestParam(name = "language") String language, HttpServletRequest request, HttpServletResponse response){
        // set Language
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        localeResolver.setLocale(request, response, StringUtils.parseLocaleString(language));
        return "redirect:"+returnUrl;
    }
}
