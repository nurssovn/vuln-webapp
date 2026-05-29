package com.cybersec.vulnwebapp.controller;

import com.cybersec.vulnwebapp.model.User;
import com.cybersec.vulnwebapp.web.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final SessionHelper sessionHelper;

    public HomeController(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        User user = sessionHelper.getCurrentUser(session);
        model.addAttribute("user", user);
        return "index";
    }
}
