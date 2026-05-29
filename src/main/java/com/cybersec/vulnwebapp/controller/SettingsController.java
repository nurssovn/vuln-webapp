package com.cybersec.vulnwebapp.controller;

import com.cybersec.vulnwebapp.model.User;
import com.cybersec.vulnwebapp.repository.UserRepository;
import com.cybersec.vulnwebapp.web.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SettingsController {

    private final UserRepository userRepository;
    private final SessionHelper sessionHelper;

    public SettingsController(UserRepository userRepository, SessionHelper sessionHelper) {
        this.userRepository = userRepository;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/settings")
    public String settings(HttpSession session, Model model) {
        User user = sessionHelper.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "settings";
    }

    @PostMapping("/settings/password")
    public String changePassword(@RequestParam Long userId,
                                 @RequestParam String newPassword,
                                 HttpSession session,
                                 Model model) {
        // VULNERABLE: CSRF — no token; accepts userId from form (can change anyone's password)
        User target = userRepository.findById(userId).orElse(null);
        if (target == null) {
            model.addAttribute("error", "User not found");
            return "settings";
        }

        target.setPassword(newPassword);
        userRepository.save(target);

        User current = sessionHelper.getCurrentUser(session);
        if (current != null && current.getId().equals(target.getId())) {
            session.setAttribute(SessionHelper.SESSION_USER, target);
        }

        model.addAttribute("user", current);
        model.addAttribute("success", "Password updated for " + target.getUsername());
        return "settings";
    }
}
