package com.example.coursesystem.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import java.io.Serializable;

public class JwtToken implements AuthenticationToken, Serializable {
    private String token;

    public JwtToken(String jwt) {
        this.token = jwt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
