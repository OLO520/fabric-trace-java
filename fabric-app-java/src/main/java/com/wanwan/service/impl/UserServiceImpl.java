package com.wanwan.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanwan.dao.FabricDao;
import com.wanwan.entity.User;
import com.wanwan.mapper.UserMapper;
import com.wanwan.util.JwtUtils;
import com.wanwan.util.Md5Utils;
import com.wanwan.util.SnowflakeIdGenerator;
import com.wanwan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FabricDao fabricDao;

    @Override
    public Map<String, Object> register(String username, String password, String userType) {
        Map<String, Object> response = new HashMap<>();
        if (userMapper.selectCount(new QueryWrapper<User>().eq("username", username)) >= 1) {
            response.put("code", 201);
            response.put("message", "用户名已存在");
            return response;
        }
        User user = new User();
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1);
        user.setUserId(idGenerator.nextId());
        user.setUsername(username);
        user.setPassword(password);
        user.setUserType(userType);
        user.setRealInfoHash(Md5Utils.encryptByMD5(username));
        userMapper.insert(user);
        //上链
        String txid = fabricDao.registerUser(user.getUserId().toString(), userType, user.getRealInfoHash());
        response.put("code", 200);
        response.put("message", "注册成功");
        response.put("txid", txid);
        return response;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> response = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", username).eq("password", password);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            response.put("code", 201);
            response.put("message", "用户名或密码错误");
            return response;
        }
        response.put("code", 200);
        response.put("message", "登录成功");
        response.put("jwt", JwtUtils.createToken(user.getUserId(), user.getUserType()));
        return response;
    }

    @Override
    public Map<String, Object> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "登出成功");
        return response;
    }

    @Override
    public Map<String, Object> getInfo(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Long userId = JwtUtils.getUserIdByJwtToken(request);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id", userId));
        if (user == null) {
            response.put("code", 201);
            response.put("message", "token异常");
            return response;
        }
        response.put("code", 200);
        response.put("message", "获取用户信息成功");
        response.put("userType", user.getUserType());
        response.put("username", user.getUsername());
        return response;
    }
}
