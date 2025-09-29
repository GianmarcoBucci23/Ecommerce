package com.ecommerce.project.security.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserInfoResponse {

    private Long id;

    private String username;

    private String jwtToken;

    private List<String> roles;

    public UserInfoResponse(Long id, String username, String jwtToken, List<String> roles) {
        this.id = id;
        this.username = username;
        this.jwtToken = jwtToken;
        this.roles = roles;
    }

    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
