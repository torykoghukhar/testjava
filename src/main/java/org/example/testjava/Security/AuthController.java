package org.example.testjava.Security;

import org.example.testjava.Services.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final RegisterService registerService;

    @Autowired
    public AuthController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/login")
    public String login() {
        logger.info("Accessed login page");
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        logger.info("Accessed registration page");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        logger.info("Registering new user: {}", username);
        try {
            registerService.registerUser(username, password);
            logger.info("User {} registered successfully", username);
            return "redirect:/auth/login";
        } catch (Exception e) {
            logger.error("Registration failed for user {}: {}", username, e.getMessage());
            return "register";
        }
    }
}


