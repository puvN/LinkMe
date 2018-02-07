package com.puvn.controller;

import com.puvn.common.mail.MailSender;
import com.puvn.common.mail.WelcomeEmailTemplate;
import com.puvn.common.validation.ReCaptchaValidator;
import com.puvn.common.validation.UserValidator;
import com.puvn.dao.UserDAO;
import com.puvn.models.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;


@Controller
public class RegisterUserController {

    @Autowired
    MessageSource messageSource;

    private ApplicationContext context =
            new ClassPathXmlApplicationContext("Spring-Module.xml");
    private UserDAO userDAO = (UserDAO) context.getBean("userDAO");
    ModelAndView modelAndView = new ModelAndView("redirect:/");

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView checkUser(@RequestParam("login") String login,
                                   @RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   @RequestParam("matchingPassword") String matchingPassword,
                                   @RequestParam("g-recaptcha-response") String g_recaptcha_response,
                                   RedirectAttributes redirectAttributes) throws IOException, SQLException {
        if (new ReCaptchaValidator().isValid(g_recaptcha_response)) {
            UserClass user = new UserClass(login, password, matchingPassword, email);
            if (new UserValidator().isValid(user)) {
                if (userDAO.findByLogin(login) != null) {
                    redirectAttributes.addFlashAttribute("error",
                            messageSource.getMessage("label.user", null, "User", LocaleContextHolder.getLocale()) +
                                    " " + login + " " +
                                    messageSource.getMessage("label.alreadyExists", null, "already exists", LocaleContextHolder.getLocale()));
                } else {
                    registerUser(user);
                    new MailSender().sendMail(new WelcomeEmailTemplate(user));
                    redirectAttributes.addFlashAttribute("msg",
                            messageSource.getMessage("label.user", null, "User", LocaleContextHolder.getLocale()) +
                                    " " + login + " " +
                                    messageSource.getMessage("label.registered", null,
                                            "You are subscribed", LocaleContextHolder.getLocale()));
                }
            } else {
                redirectAttributes.addFlashAttribute("error",
                        messageSource.getMessage("label.registrationError", null, "registration error", LocaleContextHolder.getLocale()));
            }
        }
        return this.modelAndView;
    }

    private void registerUser(UserClass user) throws SQLException {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDAO.insert(user);
    }
}
