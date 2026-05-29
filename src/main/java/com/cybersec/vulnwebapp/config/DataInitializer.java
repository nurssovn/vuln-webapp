package com.cybersec.vulnwebapp.config;

import com.cybersec.vulnwebapp.model.Comment;
import com.cybersec.vulnwebapp.model.User;
import com.cybersec.vulnwebapp.repository.CommentRepository;
import com.cybersec.vulnwebapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(UserRepository userRepository, CommentRepository commentRepository) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@vuln.local");
            admin.setBio("System administrator account.");
            admin.setRole("ADMIN");
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword("password");
            user.setEmail("user@vuln.local");
            user.setBio("Regular user account for testing.");
            user.setRole("USER");
            userRepository.save(user);

            User john = new User();
            john.setUsername("john");
            john.setPassword("1234");
            john.setEmail("john@example.com");
            john.setBio("Hello, I am John.");
            john.setRole("USER");
            userRepository.save(john);

            Comment welcome = new Comment();
            welcome.setAuthor("admin");
            welcome.setContent("Welcome to VulnShop! Post comments below.");
            commentRepository.save(welcome);
        };
    }
}
