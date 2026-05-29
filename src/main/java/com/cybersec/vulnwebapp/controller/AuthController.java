package com.cybersec.vulnwebapp.controller;

import com.cybersec.vulnwebapp.model.User;
import com.cybersec.vulnwebapp.service.VulnerableAuthService;
import com.cybersec.vulnwebapp.web.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final VulnerableAuthService authService;
    private final SessionHelper sessionHelper;

    public AuthController(VulnerableAuthService authService, SessionHelper sessionHelper) {
        this.authService = authService;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (sessionHelper.getCurrentUser(session) != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        // VULNERABLE: no rate limiting, no account lockout
        User user = authService.login(username, password);
        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("username", username);
            return "login";
        }

        sessionHelper.login(session, user);
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        sessionHelper.logout(session);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = sessionHelper.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "dashboard";
    }
}
