package com.puvn.models;

public class LinkClass {
    private String link, linkTitle, linkDate;
    private int clicked, idLink;
    private boolean isPrivate;

    public LinkClass() {}

    public LinkClass(int idLink) {
        setIdLink(idLink);
    }

    public LinkClass(String link) {
        setLink(link);
    }

    public LinkClass(String link, int idLink) {
        setLink(link); setIdLink(idLink);
    }

    public LinkClass(String link, boolean isPrivate) {
        setLink(link); setPrivate(isPrivate);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    public int getIdLink() {
        return idLink;
    }

    public void setIdLink(int idLink) {
        this.idLink = idLink;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkDate() {
        return linkDate;
    }

    public void setLinkDate(String linkDate) {
        this.linkDate = linkDate;
    }
}
