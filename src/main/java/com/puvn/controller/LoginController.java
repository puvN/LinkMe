package com.puvn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error",  messageSource.getMessage("label.wrongUsername&Pass", null,
                    "Wrong username or password", LocaleContextHolder.getLocale()));
        }
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("msg", messageSource.getMessage("label.successfulLoggedOut", null,
                "Successfully Logged out", LocaleContextHolder.getLocale()));
        return modelAndView;
    }
}
