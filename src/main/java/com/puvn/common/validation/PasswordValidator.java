package com.puvn.common.validation;

public class PasswordValidator implements Validator {

    @Override
    public boolean isValid(Object value) {
        return passwordIsValid ((String) value);
    }

    private boolean passwordIsValid(String password) {
        int minPasswordLength = 5;
        int maxPasswordLength = 20;
        return (password.length() <= maxPasswordLength &&
                password.length() >= minPasswordLength);
    }
}
