package com.martins.springOauth2.controller;

import com.martins.springOauth2.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OauthService oauthService;


    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callBack(@RequestParam String authCode) {
        return oauthService.getUserInfo(authCode);
    }
}


