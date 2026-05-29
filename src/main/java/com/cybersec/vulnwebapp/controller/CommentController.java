package com.cybersec.vulnwebapp.controller;

import com.cybersec.vulnwebapp.model.Comment;
import com.cybersec.vulnwebapp.model.User;
import com.cybersec.vulnwebapp.repository.CommentRepository;
import com.cybersec.vulnwebapp.web.SessionHelper;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    private final CommentRepository commentRepository;
    private final SessionHelper sessionHelper;

    public CommentController(CommentRepository commentRepository, SessionHelper sessionHelper) {
        this.commentRepository = commentRepository;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/comments")
    public String comments(HttpSession session, Model model) {
        User user = sessionHelper.getCurrentUser(session);
        List<Comment> comments = commentRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("comments", comments);
        return "comments";
    }

    @PostMapping("/comments")
    public String addComment(@RequestParam String content,
                             HttpSession session,
                             Model model) {
        User user = sessionHelper.getCurrentUser(session);
        String author = user != null ? user.getUsername() : "guest";

        Comment comment = new Comment();
        comment.setAuthor(author);
        // VULNERABLE: no HTML sanitization (stored XSS)
        comment.setContent(content);
        commentRepository.save(comment);

        return "redirect:/comments";
    }
}
