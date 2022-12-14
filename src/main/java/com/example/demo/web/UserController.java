package com.example.demo.web;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.web.form.UserForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {


    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;


    @GetMapping(path = "/")
    public String getAllUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/index";
    }

    @GetMapping(path = "/create")
    public String createUser(@ModelAttribute("userForm") UserForm userForm) {
        return "admin/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String storeUser(@Validated @ModelAttribute("userForm") UserForm userForm, BindingResult result) {
        if(result.hasErrors()) {
            return "admin/create";
        }
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        userService.insert(user);
        return "redirect:/";
    }

    @GetMapping(path = "/edit/{user_id}")
    public String editUser(@PathVariable("user_id") Integer id, @ModelAttribute("userForm") UserForm userForm) {
        Optional<User> userOptional = userService.selectById(id);
        User user = userOptional.get();
        BeanUtils.copyProperties("userForm", user);
        return "admin/edit";
    }

    @RequestMapping(value = "/edit/{user_id}", method = RequestMethod.POST)
    public String updateUser(@PathVariable("user_id") Integer user_id, @Validated @ModelAttribute("userForm") UserForm userForm, BindingResult result) {
        if(result.hasErrors()) {
            return "admin/edit";
        }
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        user.setId(user_id);
        userService.update(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteUser(@RequestParam Integer user_id) {
        userService.delete(user_id);
        return "redirect:/";
    }
}

