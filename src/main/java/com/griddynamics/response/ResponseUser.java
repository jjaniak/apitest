package com.griddynamics.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseUser {
    public String id;
    public String email;
    public String username;
    public String bio;
    public String image;
    public String token;
}