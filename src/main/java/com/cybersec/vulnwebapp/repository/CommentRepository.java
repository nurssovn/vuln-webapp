package com.cybersec.vulnwebapp.repository;

import com.cybersec.vulnwebapp.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
