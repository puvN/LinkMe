package com.puvn.dao;

import com.puvn.common.security.password.PasswordResetToken;
import com.puvn.models.UserClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface UserDAO {

    public void insert(UserClass user) throws SQLException;

    public UserClass findByLogin(String name);

    public ArrayList<UserClass> findSimilarUsers(String name);

    public ArrayList<UserClass> getSubscriptions(String name);

    public void addSubscription(UserClass user, UserClass subscription);

    public void deleteSubscription(UserClass user, UserClass subscription);

    public void changePassword(UserClass user);

    public String getPassword(UserClass user);

    public void setAvatarLink(UserClass user);

    public void changeEmail(UserClass user);

    public LinkedHashMap getUpdates(ArrayList<String> subscriptions);

    public void setPasswordResetToken(UserClass user, PasswordResetToken token);

    public PasswordResetToken getPasswordResetToken(UserClass user);

    public void deletePasswordResetToken (UserClass user);
}
