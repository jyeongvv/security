package io.playdata.security.contents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/contents")
public class ContentsController {
    @GetMapping("/basic")
    public String getBasic(Model model) {
        model.addAttribute("grade", "베이직 등급");
        return "contents";
    }
    @GetMapping("/premium")
    public String getPremium(Model model) {
        model.addAttribute("grade", "프리미엄 등급");
        return "contents";
    }
}