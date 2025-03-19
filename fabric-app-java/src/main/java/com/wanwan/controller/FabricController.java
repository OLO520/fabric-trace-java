package com.wanwan.controller;

import lombok.AllArgsConstructor;
import com.wanwan.config.HyperLedgerFabricProperties;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/fabric")
@AllArgsConstructor
public class FabricController {

    final Gateway gateway;

    final Contract contract;

    final HyperLedgerFabricProperties hyperLedgerFabricProperties;

    /**
     * 注册用户
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam("userID") String userID, @RequestParam("userType") String userType, @RequestParam("realInfoHash") String realInfoHash) {
        try {

            contract.submitTransaction("registerUser", userID, userType, realInfoHash);
            return "User registered successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 农产品上链
     */
    @PostMapping("/uplink")
    public String uplink(@RequestParam("userID") String userID, @RequestParam("traceability_code") String traceability_code,
                         @RequestParam("arg1") String arg1, @RequestParam("arg2") String arg2,
                         @RequestParam("arg3") String arg3, @RequestParam("arg4") String arg4,
                         @RequestParam("arg5") String arg5) {
        try {

            byte[] result = contract.submitTransaction("uplink", userID, traceability_code, arg1, arg2, arg3, arg4, arg5);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 获取用户的农产品列表
     */
    @GetMapping("/getFruitList")
    public String getFruitList(@RequestParam("userID") String userID) {
        try {

            byte[] result = contract.evaluateTransaction("getFruitList", userID);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 获取所有农产品信息
     */
    @GetMapping("/getAllFruitInfo")
    public String getAllFruitInfo() {
        try {

            byte[] result = contract.evaluateTransaction("getAllFruitInfo");
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 获取农产品上链历史
     */
    @GetMapping("/getFruitHistory")
    public String getFruitHistory(@RequestParam("traceability_code") String traceability_code) {
        try {

            byte[] result = contract.evaluateTransaction("getFruitHistory", traceability_code);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}
