package com.puvn.common.mail;

import com.puvn.models.UserClass;

public class ResetPasswordTemplate extends MailTemplate {
    private String contextPath, token;

    public ResetPasswordTemplate(UserClass recipient, String contextPath, String token) {
        this.contextPath = contextPath;
        this.token = token;
        setRecipient(recipient);
        setSubject("Linkedstor: Password reset process");
        setMessage(this.setResetPasswordMessage());
    }

    private String setResetPasswordMessage() {
        String url = contextPath + "/updatePassword/" + this.getRecipient().getLogin().replaceAll(" ", "%20") + "/"
                + this.token;
        return "Dear " + this.getRecipient().getLogin() + ",\nto reset your password on linkedstor.com" +
                " you have 24hours to click the following link:\n     "
                + url + " \nIf it wasn't you, just ignore this message.";
    }
}
