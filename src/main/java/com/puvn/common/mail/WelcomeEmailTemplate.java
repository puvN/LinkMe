package com.puvn.common.mail;


import com.puvn.models.UserClass;

public class WelcomeEmailTemplate extends MailTemplate{

    public WelcomeEmailTemplate(UserClass recipient) {
        setRecipient(recipient);
        setSubject("Welcome to " + getApplicationContext().getApplicationName() + "!");
        setMessage(setWelcomeMessage());
    }

    private String setWelcomeMessage() {
        return "Your email was registered on " + getApplicationContext().getApplicationName() + " just few seconds ago!" +
                "\nYour username is: " + getRecipient().getLogin() +
                "\nThis address will be used for password reset process, so make sure you won't loose your email password.";
    }
}
