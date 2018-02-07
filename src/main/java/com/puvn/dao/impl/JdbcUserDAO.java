package com.puvn.dao.impl;

import com.puvn.common.security.password.PasswordResetToken;
import com.puvn.dao.UserDAO;
import com.puvn.models.LinkClass;
import com.puvn.models.UserClass;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;


import static com.puvn.dao.impl.JdbcCommon.*;

public class JdbcUserDAO implements UserDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public void insert(UserClass user) throws SQLException {
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try {
            if (connection != null) {
                connection.setAutoCommit(false);

                String sql = "INSERT INTO USERS" +
                        "(username, password) VALUES (?, ?);";

                this.jdbcTemplate.update(sql, user.getLogin(), user.getPassword());

                sql = "INSERT INTO EMAILS" +
                        "(username, email) VALUES (?, ?);";

                this.jdbcTemplate.update(sql, user.getLogin(), user.getEmail());

                sql = "INSERT INTO USER_ROLE" +
                        "(username, role) VALUES (?, 'ROLE_USER')";

                this.jdbcTemplate.update(sql, user.getLogin());

                sql = "INSERT INTO MAXLINKS" +
                        "(username) VALUES (?)";

                this.jdbcTemplate.update(sql, user.getLogin());

                sql = "INSERT INTO AVATARS" +
                        "(username) VALUES (?)";

                this.jdbcTemplate.update(sql, user.getLogin());
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }


    }

    @Override
    public UserClass findByLogin(String login) {
        String sql = "SELECT u.username, e.email, a.avatarlink FROM USERS u JOIN EMAILS e " +
                "ON u.username = e.username JOIN AVATARS a ON u.username = a.username WHERE BINARY " +
                "u.username = ? LIMIT 1";
        try {
            return (UserClass) this.jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{login},
                    new RowMapper<Object>() {
                        @Override
                        public Object mapRow(ResultSet rs, int i) throws SQLException {
                            return new UserClass(
                                    rs.getString("username"),
                                    rs.getString("email"),
                                    rs.getString("avatarlink"));
                        }
                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public ArrayList<UserClass> findSimilarUsers(String name) {
        ArrayList<UserClass> similarUsers = new ArrayList<>();

        String sql = "SELECT u.username, a.avatarlink FROM USERS u JOIN AVATARS a ON u.username = a.username " +
                "WHERE u.username COLLATE UTF8_GENERAL_CI" +
                " LIKE ? LIMIT 5";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            UserClass user;
            while (rs.next()) {
                user = new UserClass();
                user.setLogin(rs.getString("username"));
                user.setAvatarLink(rs.getString("avatarlink"));
                similarUsers.add(user);
            }
            return similarUsers;
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }


    @Override
    public ArrayList<UserClass> getSubscriptions(String name) {
        String sql = "SELECT subscription FROM SUBSCRIPTIONS WHERE BINARY username = ?";
        Connection conn = null;
        ArrayList<UserClass> subscriptions = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            UserClass user;
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                user = new UserClass();
                user.setLogin(rs.getString("subscription"));
                subscriptions.add(user);
            }
            return subscriptions;
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public void addSubscription(UserClass user, UserClass subscription) {
        this.jdbcTemplate.update("INSERT INTO SUBSCRIPTIONS(username, subscription) VALUES (?, ?)",
                user.getLogin(), subscription.getLogin());
    }

    @Override
    public void deleteSubscription(UserClass user, UserClass subscription) {
        this.jdbcTemplate.update("DELETE FROM SUBSCRIPTIONS WHERE BINARY username = ? AND BINARY subscription = ?",
                user.getLogin(), subscription.getLogin());
    }

    @Override
    public void changePassword(UserClass user) {
        this.jdbcTemplate.update("UPDATE USERS SET password = ? WHERE BINARY username = ?", user.getPassword(),
                user.getLogin());
    }

    @Override
    public String getPassword(UserClass user) {
        return this.jdbcTemplate.queryForObject("SELECT password FROM USERS WHERE BINARY username = ?",
                new Object[]{user.getLogin()}, String.class);
    }

    @Override
    public void setAvatarLink(UserClass user) {
        this.jdbcTemplate.update("UPDATE AVATARS SET avatarlink= ? WHERE BINARY username = ?",
                user.getAvatarLink(), user.getLogin());
    }

    @Override
    public void changeEmail(UserClass user) {
        this.jdbcTemplate.update("UPDATE EMAILS SET email= ? WHERE BINARY username = ?",
                user.getEmail(), user.getLogin());
    }

    @Override
    public LinkedHashMap getUpdates(ArrayList<String> subscriptions) {
        LinkedHashMap updates = new LinkedHashMap();

        //a string to be used in sql query for mysql set
        String mysqlSet = null;

        for (String subscription : subscriptions)
            mysqlSet += "'" + subscription.substring(1) + "'" + ", ";
        if (mysqlSet != null)
            mysqlSet = mysqlSet.substring(4, mysqlSet.length() - 2);

        String sql = "SELECT username, link, date, isprivate FROM LINKS WHERE username IN (" + mysqlSet +
                ") AND date BETWEEN NOW() - INTERVAL 1 DAY AND NOW() ORDER BY date desc";

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString("isprivate").equals("1")) {
                    UserClass user = new UserClass();
                    user.setLogin(rs.getString("username"));

                    LinkClass link = new LinkClass(rs.getString("link"));
                    link.setLinkDate(parseDate(rs.getString("date")));

                    updates.put(user, link);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return updates;
    }

    @Override
    public void setPasswordResetToken(UserClass user, PasswordResetToken token) {
        this.jdbcTemplate.update("INSERT INTO PASSTOKENS(username, token, expiry) VALUES (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE token = ?, expiry = ?",
                user.getLogin(), token.getTokenValue(), token.getExpiryDate(), token.getTokenValue(),
                token.getExpiryDate());
    }

    @Override
    public PasswordResetToken getPasswordResetToken(UserClass user) {
        String sql = "SELECT token, expiry FROM PASSTOKENS " + " WHERE BINARY " +
                "username = ? LIMIT 1";
        try {
            return (PasswordResetToken) this.jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{user.getLogin()},
                    new RowMapper<Object>() {
                        @Override
                        public Object mapRow(ResultSet rs, int i) throws SQLException {
                            PasswordResetToken passwordResetToken = null;
                            try {
                                passwordResetToken = new PasswordResetToken(
                                        rs.getString("token"),
                                        rs.getString("expiry")
                                );
                            } catch (ParseException p) {
                                p.printStackTrace();
                            }
                            return passwordResetToken;
                        }
                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void deletePasswordResetToken(UserClass user) {
        this.jdbcTemplate.update("DELETE FROM PASSTOKENS WHERE BINARY username = ?", user.getLogin());
    }
}
