package com.puvn.dao;

import com.puvn.models.LinkClass;

import java.util.ArrayList;

public interface LinkDAO {
    public void addLink(String username, LinkClass linkClass);
    public void deleteLink(String username, LinkClass linkClass);
    public ArrayList<LinkClass> getLinks(String username);
    public int getMaxLinks(String username);
}
