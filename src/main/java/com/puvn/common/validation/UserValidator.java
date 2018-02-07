package com.puvn.common.validation;

import com.puvn.models.UserClass;

public class UserValidator implements Validator {
    @Override
    public boolean isValid(Object value) {
        return userIsValid((UserClass) value);
    }

    public boolean userIsValid(UserClass user) {
      return (new LoginValidator().isValid(user.getLogin()) &&
              new EmailValidator().isValid(user.getEmail()) &&
              new PasswordValidator().isValid(user.getPassword()) &&
              user.getPassword().equals(user.getMatchingPassword()));
    }
}
