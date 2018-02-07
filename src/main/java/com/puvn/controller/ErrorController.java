package com.puvn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ErrorController {
    @RequestMapping(value="/error", method = RequestMethod.POST)
    public ModelAndView showError(@RequestParam String errorMessage){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }
}
