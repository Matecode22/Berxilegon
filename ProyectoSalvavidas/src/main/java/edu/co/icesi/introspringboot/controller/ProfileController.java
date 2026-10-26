package edu.co.icesi.introspringboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping("/me")
    public String myProfile(Model model, Authentication authentication) {
        model.addAttribute("principal", authentication.getName());
        model.addAttribute("authorities", authentication.getAuthorities());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "usuario/profile";
    }
}