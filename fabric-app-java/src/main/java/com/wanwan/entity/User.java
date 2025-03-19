package com.wanwan.entity;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private String userType;
    private String realInfoHash;
    private String username;
    private String password;
}
