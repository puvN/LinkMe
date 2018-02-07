package com.puvn.controller;

import com.puvn.common.mail.MailSender;
import com.puvn.common.mail.ResetPasswordTemplate;
import com.puvn.common.security.password.PasswordResetToken;
import com.puvn.common.validation.PasswordValidator;
import com.puvn.dao.UserDAO;
import com.puvn.models.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
public class RestorePasswordController {


    private ApplicationContext context =
            new ClassPathXmlApplicationContext("Spring-Module.xml");
    private UserDAO userDAO = (UserDAO) context.getBean("userDAO");
    private UserClass user;

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public ModelAndView forgotPassword() {
        return new ModelAndView("forgotPassword");
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ModelAndView resetPassword(HttpServletRequest request, @RequestParam("username") String username,
                                      RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        UserClass user = userDAO.findByLogin(username);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("label.noSuchUser",
                    null, "No such user", LocaleContextHolder.getLocale()));
            return modelAndView;
        }
        PasswordResetToken resetToken = new PasswordResetToken();
        new MailSender().sendMail(new ResetPasswordTemplate(user, appUrl, resetToken.getTokenValue()));
        userDAO.setPasswordResetToken(user, resetToken);
        redirectAttributes.addFlashAttribute("msg", messageSource.getMessage("label.emailSent",
                null, "Email sent to", LocaleContextHolder.getLocale()) + " " + user.getEmail());
        return modelAndView;
    }

    @RequestMapping(value = "/updatePassword/{username}/{token}", method = RequestMethod.GET)
    public ModelAndView changePassword(@PathVariable String username, @PathVariable String token,
                                       RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        this.user = new UserClass();
        user.setLogin(username);
        PasswordResetToken userResetToken = userDAO.getPasswordResetToken(user);
        Calendar calendar = Calendar.getInstance();
        if (userResetToken != null &&
                userResetToken.getExpiryDate().getTime() - calendar.getTime().getTime() > 0 &&
                userResetToken.getTokenValue().equals(token)) {
            modelAndView.setViewName("updatePassword");
        } else {
            modelAndView.setViewName("redirect:/");
            redirectAttributes.addFlashAttribute("error", "Your password reset token is outdate or invalid");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/setNewPassword", method = RequestMethod.POST)
    public ModelAndView setNewPassword(RedirectAttributes redirectAttributes,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("matchingNewPassword") String matchingNewPassword) {
        PasswordValidator passwordValidator = new PasswordValidator();
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        if (passwordValidator.isValid(newPassword) && newPassword.equals(matchingNewPassword)) {
            this.user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userDAO.changePassword(this.user);
            userDAO.deletePasswordResetToken(this.user);
            redirectAttributes.addFlashAttribute("msg", "Password changed, you may login.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Wrong password or passwords are not the same");
        }
        return modelAndView;
    }
}
