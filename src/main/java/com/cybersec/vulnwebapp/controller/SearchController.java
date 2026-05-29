package com.cybersec.vulnwebapp.controller;

import com.cybersec.vulnwebapp.model.User;
import com.cybersec.vulnwebapp.service.VulnerableAuthService;
import com.cybersec.vulnwebapp.web.SessionHelper;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final VulnerableAuthService authService;
    private final SessionHelper sessionHelper;

    public SearchController(VulnerableAuthService authService, SessionHelper sessionHelper) {
        this.authService = authService;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q,
                         HttpSession session,
                         Model model) {
        model.addAttribute("user", sessionHelper.getCurrentUser(session));
        model.addAttribute("query", q);

        if (q != null && !q.isBlank()) {
            List<User> results = authService.searchUsers(q);
            model.addAttribute("results", results);
            // VULNERABLE: reflects raw query in page (reflected XSS potential in template)
            model.addAttribute("rawQuery", q);
        }

        return "search";
    }
}
