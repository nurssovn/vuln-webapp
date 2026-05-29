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
public class ProfileController {

    private final UserRepository userRepository;
    private final SessionHelper sessionHelper;

    public ProfileController(UserRepository userRepository, SessionHelper sessionHelper) {
        this.userRepository = userRepository;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/profile")
    public String viewProfile(@RequestParam Long id, HttpSession session, Model model) {
        // VULNERABLE: IDOR — any user can view any profile by changing id
        User profile = userRepository.findById(id).orElse(null);
        if (profile == null) {
            model.addAttribute("error", "User not found");
            return "profile";
        }

        model.addAttribute("user", sessionHelper.getCurrentUser(session));
        model.addAttribute("profile", profile);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateBio(@RequestParam String bio,
                            HttpSession session,
                            Model model) {
        User current = sessionHelper.getCurrentUser(session);
        if (current == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(current.getId()).orElseThrow();
        // VULNERABLE: stored XSS in bio field
        user.setBio(bio);
        userRepository.save(user);

        session.setAttribute(SessionHelper.SESSION_USER, user);
        return "redirect:/profile?id=" + user.getId();
    }
}
