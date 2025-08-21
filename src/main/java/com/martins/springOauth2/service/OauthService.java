package com.martins.springOauth2.service;

import com.martins.springOauth2.model.Elements;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class OauthService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.oauth2.client.token-endpoint}")
    private String tokenEndpoint;

    @Value("${spring.oauth2.client.redirect-url}")
    private String redirectUrl;

    @Value("${spring.oauth2.client.grant-type}")
    private String grantType;

    @Value("${spring.oauth2.client.token-info-url}")
    private String tokenInfoUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> getUserInfo(String authCode) {

        Elements elements = Elements.builder()
                .code(authCode)
                .clientId(clientId)
                .clientSecurity(clientSecret)
                .redirectUrl(redirectUrl)
                .grantType(grantType)
                .build();
//            refresh_token

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(elements.getMultiValueMap(), headers);
            log.info("request: {}", request);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            log.info("tokenResp: {}", tokenResponse);

            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoUrl = tokenInfoUrl + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
            log.info("userInfo: {}", userInfoResponse);

            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                log.info("email: {}", email);
//                UserDetails userDetails = null;
                try {
//                    userDetails = userDetailsService.loadUserByUsername(email);
                } catch (Exception e) {
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
//https://www.googleapis.com/openid
