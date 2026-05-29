package com.cybersec.vulnwebapp.web;

import com.cybersec.vulnwebapp.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionHelper {

    public static final String SESSION_USER = "loggedInUser";

    public User getCurrentUser(HttpSession session) {
        Object value = session.getAttribute(SESSION_USER);
        if (value instanceof User user) {
            return user;
        }
        return null;
    }

    public void login(HttpSession session, User user) {
        // VULNERABLE: no session regeneration after login (session fixation)
        session.setAttribute(SESSION_USER, user);
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
