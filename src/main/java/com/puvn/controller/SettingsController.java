package com.puvn.controller;

import com.puvn.common.validation.EmailValidator;
import com.puvn.dao.UserDAO;
import com.puvn.models.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.puvn.common.validation.PasswordValidator;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {
    private ApplicationContext context =
            new ClassPathXmlApplicationContext("Spring-Module.xml");
    private UserDAO userDAO = (UserDAO) context.getBean("userDAO");
    private ModelAndView modelAndView;

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView showSettings(HttpSession session) {
        this.modelAndView = new ModelAndView();
        this.modelAndView.setViewName("settings");
        modelAndView.addObject("email", session.getAttribute("yourMail"));
        this.modelAndView.addObject("avatarLink", session.getAttribute("yourAvatarLink"));
        return this.modelAndView;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    private ModelAndView changePassword(@RequestParam String oldPassword,
                                        @RequestParam String newPassword,
                                        @RequestParam String matchingNewPassword,
                                        HttpServletRequest request, RedirectAttributes redirectAttributes) throws ServletException {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            this.modelAndView = new ModelAndView("redirect:/settings");
            PasswordValidator passwordValidator = new PasswordValidator();
            if (passwordValidator.isValid(oldPassword) &&
                    passwordValidator.isValid(newPassword) &&
                    passwordValidator.isValid(matchingNewPassword) &&
                    newPassword.equals(matchingNewPassword)) {
                UserClass user = new UserClass();
                user.setLogin(request.getRemoteUser());
                user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
                if (new BCryptPasswordEncoder().matches(oldPassword, userDAO.getPassword(user))) {
                    userDAO.changePassword(user);
                    redirectAttributes.addFlashAttribute("msg",
                            messageSource.getMessage("label.passwordChanged", null, "Password Changed", LocaleContextHolder.getLocale()));
                } else
                    redirectAttributes.addFlashAttribute("error",
                            messageSource.getMessage("label.wrongPassword", null, "Wrong password", LocaleContextHolder.getLocale()));
            }
        } else
            redirectAttributes.addFlashAttribute("msg",
                    messageSource.getMessage("label.errorPwChanging", null, "Error during password changing", LocaleContextHolder.getLocale()));
        return this.modelAndView;
    }

    @RequestMapping(value = "/changeEmail", method = RequestMethod.POST)
    private ModelAndView changeEmail(@RequestParam String newEmail,
                                     RedirectAttributes redirectAttributes,
                                     HttpServletRequest request, HttpSession session) throws ServletException {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            this.modelAndView = new ModelAndView("redirect:/settings");
            EmailValidator emailValidator = new EmailValidator();
            if (emailValidator.isValid(newEmail)) {
                UserClass user = new UserClass(request.getRemoteUser(), newEmail);
                userDAO.changeEmail(user);
                session.setAttribute("yourMail", newEmail);
                redirectAttributes.addFlashAttribute("msg",
                        messageSource.getMessage("label.emailChanged", null, "Email changed", LocaleContextHolder.getLocale()));
            } else {
                redirectAttributes.addFlashAttribute("error",
                        messageSource.getMessage("label.emailHint", null, "Enter correct email", LocaleContextHolder.getLocale()));
            }
        } else request.logout();
        return this.modelAndView;
    }

    @RequestMapping(value = "/setAvatarLink", method = RequestMethod.POST)
    private ModelAndView setAvatarLink(@RequestParam String newAvatarLink, HttpServletRequest request,
                                       HttpSession session)
            throws ServletException {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            this.modelAndView = new ModelAndView("redirect:/settings");
            UserClass user = new UserClass(request.getRemoteUser(), null, newAvatarLink);
            userDAO.setAvatarLink(user);
            session.setAttribute("yourAvatarLink", newAvatarLink);
        } else request.logout();
        return this.modelAndView;
    }

}
