package com.wanwan.controller;


import com.wanwan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestParam("username") String username, @RequestParam("password") String password,@RequestParam("userType") String userType) {
        Map<String, Object> response = userService.register(username,password,userType);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Map<String, Object> response = userService.login(username,password);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> response = userService.logout();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getInfo")
    public ResponseEntity<Map<String, Object>> getInfo(HttpServletRequest request) {
        Map<String, Object> response = userService.getInfo(request);
        return ResponseEntity.ok(response);
    }
}
