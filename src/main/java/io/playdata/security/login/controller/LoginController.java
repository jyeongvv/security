package io.playdata.security.login.controller;

import io.playdata.security.login.model.AccountDTO;
import io.playdata.security.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.GeneratedValue;

@Controller // spring에 등록
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    private String postRegister(
            AccountDTO user,
            RedirectAttributes redirectAttributes
    ) {
        // throws -> 에러처리 상위 메소드로 위임
        try {
            loginService.register(user);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
            return "redirect:/register";
        }
        redirectAttributes.addFlashAttribute("msg", "정상적으로 등록됐습니다");
        return "redirect:/login"; // ~/login 페이지로
    }
    @GetMapping("register")
    public  String getRegister(Model model) {
        model.addAttribute("user", new AccountDTO());
        return "register";
    }

    @GetMapping
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
