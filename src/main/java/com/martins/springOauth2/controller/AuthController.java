package com.martins.springOauth2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callBack(@RequestParam String authCode) {
        try {
            String tokenEndpoint = "https://oauth2.googleapis.com/token";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", authCode);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", "https://developers.google.com/oauthplayground");
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            log.info("request: {}",request);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            log.info("tokenResp: {}",tokenResponse);
            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
            log.info("userInfo: {}",userInfoResponse);

            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                log.info("email: {}",email);
//                UserDetails userDetails = null;
                try{
//                    userDetails = userDetailsService.loadUserByUsername(email);
                }catch (Exception e){
//                    User user = new User();
//                    user.setEmail(email);
//                    user.setUserName(email);
//                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
//                    user.setRoles(Arrays.asList("USER"));
//                    userRepository.save(user);
                }
//                String jwtToken = jwtUtil.generateToken(email);
//                return ResponseEntity.ok(Collections.singletonMap("token", jwtToken));
                return ResponseEntity.ok(userInfoResponse);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            log.error("Exception occurred while handleGoogleCallback ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


//https://www.googleapis.com/auth/userinfo.email
//https://www.googleapis.com/auth/userinfo.profile