package com.puvn.models;


public class UserClass {

    private String login;

    private String password, matchingPassword;

    private String email;

    private String avatarLink;

    public UserClass() {}

    public UserClass(String login, String email) {
        setLogin(login);
        setEmail(email);
    }

    public UserClass(String login, String email, String avatarLink) {
        setLogin(login);
        setEmail(email);
        setAvatarLink(avatarLink);
    }

    public UserClass(String login, String password, String matchingPassword, String email) {
        setLogin(login);
        setPassword(password);
        setEmail(email);
        setMatchingPassword(matchingPassword);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
       this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }
}
