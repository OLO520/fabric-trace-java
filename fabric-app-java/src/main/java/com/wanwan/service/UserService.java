package com.wanwan.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserService {
    Map<String, Object> register(String username, String password, String userType);
    Map<String, Object> login(String username, String password);
    Map<String, Object> logout();
    Map<String, Object> getInfo(HttpServletRequest request);
}
