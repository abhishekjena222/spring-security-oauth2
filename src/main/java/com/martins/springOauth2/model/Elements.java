package com.martins.springOauth2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Elements {
    String code;
    String clientId;
    String clientSecurity;
    String redirectUrl;
    String grantType;

    public MultiValueMap<String ,String> getMultiValueMap(){
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("code",this.code);
        map.add("client_id",this.clientId);
        map.add("client_security",this.clientSecurity);
        map.add("redirect_url",this.redirectUrl);
        map.add("grant_type",this.grantType);
        return map;
    }
}
