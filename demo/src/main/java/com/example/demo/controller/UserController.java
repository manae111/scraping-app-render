package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.User;
import com.example.demo.form.UserForm;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    // 新規登録 user
    @GetMapping("/toRegister")
    public String toRegister(UserForm userForm) {
        session.invalidate();
        return "register";
    }

    @PostMapping("/register")
    public String register(@Validated UserForm userForm,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", userForm); 
            return "register";
        }
        if (userService.findUserByUsername(userForm.getUsername()) != null) {
            model.addAttribute("message", "このメールアドレスは既に登録されています");
            return "register";
        }
        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setPassword(userForm.getPassword());
        userService.insertUser(user);
        return "redirect:/toLogin";
    }
}
