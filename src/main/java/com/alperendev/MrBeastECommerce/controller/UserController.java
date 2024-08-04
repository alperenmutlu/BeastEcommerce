package com.alperendev.MrBeastECommerce.controller;

import com.alperendev.MrBeastECommerce.dto.Response;
import com.alperendev.MrBeastECommerce.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/my-info")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getUserInfoAndOrderHistory(){
        return ResponseEntity.ok(userService.getUserInfoAndOrderHistory());
    }

}
