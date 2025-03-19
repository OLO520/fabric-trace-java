package com.wanwan.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanwan.dao.FabricDao;
import com.wanwan.entity.User;
import com.wanwan.mapper.UserMapper;
import com.wanwan.util.JwtUtils;
import com.wanwan.service.TraceService;
import com.wanwan.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class TraceServiceImpl implements TraceService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FabricDao fabricDao;


    @Override
    public Map<String, Object> uplink(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1);
            String farmerTraceabilityCode = String.valueOf(idGenerator.nextId()).substring(2);
            List<String> args = buildArgs(request, farmerTraceabilityCode);
            String res = fabricDao.uplink(args.toArray(new String[0]));
            response.put("code", 200);
            response.put("message", "uplink success");
            response.put("txid", res);
            response.put("traceability_code", args.get(1));
        } catch (Exception e) {
            response.put("code", 201);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @Override
    public Map<String, Object> getFruitInfo(String traceabilityCode) {
        Map<String, Object> response = new HashMap<>();
        String res = null;
        try {
            res = fabricDao.getFruitInfo(traceabilityCode);
        } catch (Exception e) {
            response.put("code", 201);
            response.put("message", e.getMessage());
        }
        response.put("code", 200);
        response.put("message", "query success");
        response.put("data", res);
        return response;
    }

    @Override
    public Map<String, Object> getFruitList(HttpServletRequest request) {
        Long userId = JwtUtils.getUserIdByJwtToken(request);
        Map<String, Object> response = new HashMap<>();
        String res = null;
        try {
            res = fabricDao.getFruitList(userId.toString());
        } catch (Exception e) {
            response.put("code", 201);
            response.put("message", e.getMessage());
        }
        response.put("code", 200);
        response.put("message", "query success");
        response.put("data", res);
        return response;
    }

    @Override
    public Map<String, Object> getAllFruitInfo() {
        Map<String, Object> response = new HashMap<>();
        String res = null;
        try {
            res = fabricDao.getAllFruitInfo();
        } catch (Exception e) {
            response.put("code", 201);
            response.put("message", e.getMessage());
        }
        response.put("code", 200);
        response.put("message", "query success");
        response.put("data", res);
        return response;
    }

    @Override
    public Map<String, Object> getFruitHistory(String traceabilityCode) {
        Map<String, Object> response = new HashMap<>();
        String res = null;
        try {
            res = fabricDao.getFruitHistory(traceabilityCode);
        } catch (Exception e) {
            response.put("code", 201);
            response.put("message", e.getMessage());
        }
        response.put("code", 200);
        response.put("message", "query success");
        response.put("data", res);
        return response;
    }

    private List<String> buildArgs(HttpServletRequest request, String farmerTraceabilityCode) {
        List<String> args = new ArrayList<>();
        Long userId = JwtUtils.getUserIdByJwtToken(request);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id", userId));
        if (user == null) {
            throw new RuntimeException("token异常");
        }
        String userType = user.getUserType();
        args.add(userId.toString());
        if ("种植户".equals(userType)) {
            args.add(farmerTraceabilityCode);
        } else {
            String traceabilityCode = request.getParameter("traceability_code");
            String res = fabricDao.getFruitInfo(traceabilityCode);
            if (res == null || res.isEmpty() || traceabilityCode.length() != 16) {
                throw new RuntimeException("请检查溯源码是否正确!!");
            } else {
                args.add(traceabilityCode);
            }
        }
        args.add(request.getParameter("arg1"));
        args.add(request.getParameter("arg2"));
        args.add(request.getParameter("arg3"));
        args.add(request.getParameter("arg4"));
        args.add(request.getParameter("arg5"));
        return args;
    }
}
