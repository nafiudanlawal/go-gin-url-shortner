package com.nafiudanlawal.spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FrontendController {
    @GetMapping("/")
    public String createUrl(){
        return "index";
    }
    @GetMapping("/{code}")
    public String createUrl(@PathVariable("code") String code, Model model){
        model.addAttribute("code", code);
        return "showUrl";
    }
}
