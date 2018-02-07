package com.puvn.common.mail;

import com.puvn.common.context.AppContext;
import com.puvn.models.UserClass;
import org.springframework.context.ApplicationContext;

public class MailTemplate {

    private UserClass recipient;
    private String subject, message;

    public MailTemplate() {}

    protected UserClass getRecipient() {
        return recipient;
    }

    protected String getSubject() {
        return subject;
    }

    protected void setSubject(String subject) {
        this.subject = subject;
    }

    protected String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected void setRecipient(UserClass recipient) {
        this.recipient = recipient;
    }

    public ApplicationContext getApplicationContext() {
        return AppContext.getApplicationContext();
    }
}
