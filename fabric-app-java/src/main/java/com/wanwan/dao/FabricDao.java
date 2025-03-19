package com.wanwan.dao;

import org.hyperledger.fabric.client.Contract;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Service
public class FabricDao {

    @Resource
    Contract contract;


    /**
     * 注册用户
     */
    public String registerUser(String userID,  String userType,String realInfoHash) {
        try {
            byte[] result = contract.submitTransaction("registerUser", userID, userType, realInfoHash);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("区块链异常，上链失败");
        }
    }

    /**
     * 农产品上链
     */
    public String uplink(String[] args) {
        try {
            byte[] result = contract.submitTransaction("uplink", args);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("区块链异常，上链失败");
        }
    }

    /**
     * 获取用户的农产品列表
     */
    public String getFruitList(String userID) {
        try {

            byte[] result = contract.evaluateTransaction("getFruitList", userID);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("区块链异常，查询失败");
        }
    }

    /**
     * 根据溯源码获取农产品信息
     */
    public String getFruitInfo(String traceability_code) {
        try {

            byte[] result = contract.evaluateTransaction("getFruitInfo", traceability_code);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("区块链异常，查询失败");
        }
    }

    /**
     * 获取所有农产品信息
     */
    public String getAllFruitInfo() {
        try {

            byte[] result = contract.evaluateTransaction("getAllFruitInfo");
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("区块链异常，查询失败");
        }
    }

    /**
     * 获取农产品上链历史
     */
    public String getFruitHistory(String traceability_code) {
        try {

            byte[] result = contract.evaluateTransaction("getFruitHistory", traceability_code);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("区块链异常，查询失败");
        }
    }

}
