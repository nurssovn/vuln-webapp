package com.cybersec.vulnwebapp.service;

import com.cybersec.vulnwebapp.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class VulnerableAuthService {

    private final JdbcTemplate jdbcTemplate;

    public VulnerableAuthService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * VULNERABLE: SQL Injection via string concatenation.
     * Remediation: use PreparedStatement / parameterized queries.
     */
    public User login(String username, String password) {
        String sql = "SELECT id, username, password, email, bio, role FROM users "
                + "WHERE username = '" + username + "' AND password = '" + password + "'";

        List<User> users = jdbcTemplate.query(sql, USER_MAPPER);
        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    /**
     * VULNERABLE: SQL Injection in search parameter.
     */
    public List<User> searchUsers(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }

        String sql = "SELECT id, username, password, email, bio, role FROM users "
                + "WHERE username LIKE '%" + query + "%' "
                + "OR email LIKE '%" + query + "%' "
                + "OR bio LIKE '%" + query + "%'";

        return jdbcTemplate.query(sql, USER_MAPPER);
    }

    private static final RowMapper<User> USER_MAPPER = (ResultSet rs, int rowNum) -> mapUser(rs);

    private static User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setBio(rs.getString("bio"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
