package com.cybersec.vulnwebapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PhishingController {

    private static final Logger log = LoggerFactory.getLogger(PhishingController.class);
    private static final Path LOG_FILE = Path.of("data", "phishing-captures.log");

    @GetMapping("/phishing/login")
    public String fakeLoginPage() {
        // VULNERABLE: phishing page mimics real login (for authorized simulation only)
        return "phishing-login";
    }

    @PostMapping("/phishing/login")
    public String captureCredentials(@RequestParam String username,
                                     @RequestParam String password,
                                     Model model) throws IOException {
        String entry = LocalDateTime.now() + " | username=" + username + " | password=" + password + System.lineSeparator();
        log.warn("PHISHING SIMULATION captured: {}", entry.trim());

        Files.createDirectories(LOG_FILE.getParent());
        Files.writeString(LOG_FILE, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        model.addAttribute("message", "Session expired. Please log in again at the real site.");
        return "phishing-login";
    }
}
