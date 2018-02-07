package com.puvn.common.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginValidator implements Validator {
    @Override
    public boolean isValid(Object value) {
        return loginIsValid((String) value);
    }

    private boolean loginIsValid(String login) {
        int minLoginLength = 1,
            maxLoginLength = 21;
        /*String reg = "^[a-zA-Z0-9 ]+$/i";
        Pattern loginPattern = Pattern.compile(reg);
        Matcher loginMatcher = loginPattern.matcher(login);

        System.out.println(login.length() < maxLoginLength &&
                login.length() > minLoginLength &&
                loginMatcher.find());*/

        return
        (
                login.length() < maxLoginLength &&
                login.length() > minLoginLength
        );
    }
}
