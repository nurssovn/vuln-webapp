package com.cybersec.vulnwebapp.controller;

import com.cybersec.vulnwebapp.model.User;
import com.cybersec.vulnwebapp.repository.UserRepository;
import com.cybersec.vulnwebapp.web.SessionHelper;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final SessionHelper sessionHelper;

    public AdminController(UserRepository userRepository, SessionHelper sessionHelper) {
        this.userRepository = userRepository;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/admin")
    public String adminPanel(@RequestParam(required = false) Boolean admin,
                             HttpSession session,
                             Model model) {
        User user = sessionHelper.getCurrentUser(session);

        // VULNERABLE: broken access control via query parameter ?admin=true
        boolean isAdmin = (admin != null && admin)
                || (user != null && "ADMIN".equals(user.getRole()));

        if (!isAdmin) {
            model.addAttribute("error", "Access denied. Try ?admin=true (intentional flaw for demo).");
            model.addAttribute("user", user);
            return "admin";
        }

        List<User> users = userRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        model.addAttribute("accessGranted", true);
        return "admin";
    }
}
