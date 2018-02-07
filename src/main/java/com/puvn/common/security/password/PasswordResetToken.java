package com.puvn.common.security.password;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class PasswordResetToken {
    private String token;

    private Date expiryDate;

    public PasswordResetToken() {
        this.expiryDate = calculateExpiryDate();
        this.token = createTokenValue();
    }

    public PasswordResetToken(String token, String expiry) throws ParseException {
        this.token = token;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.expiryDate = format.parse(expiry);
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getTokenValue() {
        return token;
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    private String createTokenValue() {
        return UUID.randomUUID().toString();
    }
}