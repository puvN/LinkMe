package com.puvn.controller;

import com.puvn.models.UserClass;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BaseController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView newUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("command", new UserClass());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/terms", method = RequestMethod.GET)
    public ModelAndView showTerms() {
        return new ModelAndView("terms");
    }
}