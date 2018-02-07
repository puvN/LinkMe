package com.puvn.dao.impl;

import com.puvn.dao.LinkDAO;
import com.puvn.models.LinkClass;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.puvn.dao.impl.JdbcCommon.*;


public class JdbcLinksDAO implements LinkDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public void addLink(String username, LinkClass linkClass) {
        String isPrivate = (linkClass.isPrivate()) ? "1" : "0";
        this.jdbcTemplate.update("INSERT INTO LINKS(username, link, isprivate, linkTitle) VALUES (?, ?, ?, ?);",
                username, linkClass.getLink(), isPrivate, linkClass.getLinkTitle());
    }

    @Override
    public void deleteLink(String username, LinkClass linkClass) {
        this.jdbcTemplate.update("DELETE FROM LINKS WHERE username = ? AND idLink = ? LIMIT 1",
                username, String.valueOf(linkClass.getIdLink()));
    }

    @Override
    public ArrayList<LinkClass> getLinks(String username) {
        ArrayList<LinkClass> links = new ArrayList<>();
        String sql = "SELECT idLink, link, isprivate, linkTitle, date FROM LINKS WHERE username = ?";

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LinkClass link = new LinkClass(rs.getString("link"), rs.getInt("idLink"));
                link.setLinkTitle(rs.getString("linkTitle"));
                link.setLinkDate(parseDate(rs.getString("date")));
                if (rs.getString("isprivate").equals("1")) link.setPrivate(true);
                else link.setPrivate(false);
                links.add(link);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return links;
    }

    @Override
    public int getMaxLinks(String username) {
        return this.jdbcTemplate.queryForObject("SELECT maxlinks from MAXLINKS where username = ? LIMIT 1",
                new Object[]{username}, Integer.class);
    }
}
