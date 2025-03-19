package com.wanwan;

import com.alibaba.fastjson.JSON;
import lombok.extern.java.Log;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Contract(
        name = "SmartContract",
        info = @Info(
                title = "Fruit Traceability Contract",
                description = "A smart contract for fruit traceability",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.smart@example.com",
                        name = "F Smart",
                        url = "https://hyperledger.example.com"))
)
@Default
@Log
public class SmartContract implements ContractInterface {


    // 注册用户
    @Transaction()
    public String registerUser(Context ctx, String userID, String userType, String realInfoHash) {
        ChaincodeStub stub = ctx.getStub();
        User user = new User(userID, userType, realInfoHash);
        try {
            String json = JSON.toJSONString(user);
            stub.putStringState(userID, json);
            return stub.getTxId();
        } catch (Exception e) {
            log.info("Failed to register user: " + e);
            throw new ChaincodeException("Failed to register user: " + e.getMessage());
        }
    }

    // 农产品上链
    @Transaction()
    public String uplink(Context ctx, String userID, String traceability_code, String arg1, String arg2, String arg3, String arg4, String arg5) {
        ChaincodeStub stub = ctx.getStub();
        String userType;
        try {
            userType = getUserType(ctx, userID);
        } catch (Exception e) {
            throw new ChaincodeException("Failed to get user type: " + e.getMessage());
        }

        Fruit fruit = getFruitInfo(ctx, traceability_code);
        fruit.setTraceability_code(traceability_code);

        String txid = stub.getTxId();
        String timestamp = getFormattedTimestamp(stub);

        switch (userType) {
            case "种植户":
                FarmerInput farmerInput = fruit.getFarmer_input();
                if (farmerInput == null) {
                    farmerInput = new FarmerInput();
                }
                farmerInput.setFa_fruitName(arg1);
                farmerInput.setFa_origin(arg2);
                farmerInput.setFa_plantTime(arg3);
                farmerInput.setFa_pickingTime(arg4);
                farmerInput.setFa_farmerName(arg5);
                farmerInput.setFa_txid(txid);
                farmerInput.setFa_timestamp(timestamp);
                fruit.setFarmer_input(farmerInput);
                break;
            case "工厂":
                FactoryInput factoryInput = fruit.getFactory_input();
                if (factoryInput == null) {
                    factoryInput = new FactoryInput();
                }
                factoryInput.setFac_productName(arg1);
                factoryInput.setFac_productionbatch(arg2);
                factoryInput.setFac_productionTime(arg3);
                factoryInput.setFac_factoryName(arg4);
                factoryInput.setFac_contactNumber(arg5);
                factoryInput.setFac_txid(txid);
                factoryInput.setFac_timestamp(timestamp);
                fruit.setFactory_input(factoryInput);
                break;
            case "运输司机":
                DriverInput driverInput = fruit.getDriver_input();
                if (driverInput == null) {
                    driverInput = new DriverInput();
                }
                driverInput.setDr_name(arg1);
                driverInput.setDr_age(arg2);
                driverInput.setDr_phone(arg3);
                driverInput.setDr_carNumber(arg4);
                driverInput.setDr_transport(arg5);
                driverInput.setDr_txid(txid);
                driverInput.setDr_timestamp(timestamp);
                fruit.setDriver_input(driverInput);
                break;
            case "商店":
                ShopInput shopInput = fruit.getShop_input();
                if (shopInput == null) {
                    shopInput = new ShopInput();
                }
                shopInput.setSh_storeTime(arg1);
                shopInput.setSh_sellTime(arg2);
                shopInput.setSh_shopName(arg3);
                shopInput.setSh_shopAddress(arg4);
                shopInput.setSh_shopPhone(arg5);
                shopInput.setSh_txid(txid);
                shopInput.setSh_timestamp(timestamp);
                fruit.setShop_input(shopInput);
                break;
            default:
                throw new ChaincodeException("Invalid user type");
        }

        try {
            String json = JSON.toJSONString(fruit);
            stub.putStringState(traceability_code, json);
            addFruit(ctx, userID, fruit);
        } catch (Exception e) {
            log.info("Failed to uplink fruit: " + e);
            throw new ChaincodeException("Failed to uplink fruit: " + e.getMessage());
        }

        return txid;
    }

    // 添加农产品到用户的农产品列表
    private void addFruit(Context ctx, String userID, Fruit fruit) {
        ChaincodeStub stub = ctx.getStub();
        User user = getUserInfo(ctx, userID);
        user.addFruit(fruit);
        try {
            String json = JSON.toJSONString(user);
            stub.putStringState(userID, json);
        } catch (Exception e) {
            log.info("Failed to add fruit to user: " + e);
            throw new ChaincodeException("Failed to add fruit to user: " + e.getMessage());
        }
    }

    // 获取用户类型
    private String getUserType(Context ctx, String userID) {
        ChaincodeStub stub = ctx.getStub();
        byte[] userBytes = stub.getState(userID);
        if (userBytes == null || userBytes.length == 0) {
            throw new ChaincodeException("The user " + userID + " does not exist");
        }
        try {
            User user = JSON.parseObject(userBytes, User.class);
            return user.getUserType();
        } catch (Exception e) {
            log.info("Failed to get user type: " + e);
            throw new ChaincodeException("Failed to get user type: " + e.getMessage());
        }
    }

    // 获取用户信息
    public User getUserInfo(Context ctx, String userID) {
        ChaincodeStub stub = ctx.getStub();
        byte[] userBytes = stub.getState(userID);
        if (userBytes == null || userBytes.length == 0) {
            throw new ChaincodeException("The user " + userID + " does not exist");
        }
        try {
            return JSON.parseObject(userBytes, User.class);
        } catch (Exception e) {
            log.info("Failed to get user info: " + e);

            throw new ChaincodeException("Failed to get user info: " + e.getMessage());
        }
    }

    // 获取农产品的上链信息
    @Transaction()
    public Fruit getFruitInfo(Context ctx, String traceability_code) {
        ChaincodeStub stub = ctx.getStub();
        byte[] fruitBytes = stub.getState(traceability_code);
        if (fruitBytes == null || fruitBytes.length == 0) {
            return new Fruit();
        }
        try {
            return JSON.parseObject(fruitBytes, Fruit.class);
        } catch (Exception e) {
            log.info("Failed to get fruit info: " + e);
            throw new ChaincodeException("Failed to get fruit info: " + e.getMessage());
        }
    }

    // 获取用户的农产品ID列表
    @Transaction()
    public Fruit[] getFruitList(Context ctx, String userID) {
        User user = getUserInfo(ctx, userID);
        List<Fruit> fruitList = user.getFruitList();
        if (fruitList == null) {
            return new Fruit[0];
        }
        return fruitList.toArray(new Fruit[fruitList.size()]);
    }

    // 获取所有的农产品信息
    @Transaction()
    public Fruit[] getAllFruitInfo(Context ctx) throws Exception {
        ChaincodeStub stub = ctx.getStub();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        List<Fruit> fruits = new ArrayList<>();
        try {
            for (KeyValue result : results) {
                try {
                    log.info("result StringValue: " + result.getStringValue());
                    Fruit fruit = JSON.parseObject(result.getStringValue(), Fruit.class);
                    if (fruit.getTraceability_code() != null && !fruit.getTraceability_code().isEmpty()) {
                        fruits.add(fruit);
                    }
                } catch (Exception e) {
                    // 如果解析失败，则跳过该条记录
                    log.info("****************Failed to get all fruit info: " + e);
                    continue;
                }
            }
        } catch (Exception e) {
            throw new ChaincodeException("Failed to get all fruit info: " + e.getMessage());
        } finally {
            results.close();
        }
        return fruits.toArray(new Fruit[fruits.size()]);
    }

    // 获取农产品上链历史
    @Transaction()
    public HistoryQueryResult[] getFruitHistory(Context ctx, String traceability_code) throws Exception {
        ChaincodeStub stub = ctx.getStub();
        QueryResultsIterator<KeyModification> results = stub.getHistoryForKey(traceability_code);
        log.info("HistoryForKey results: " + results.toString());
        List<HistoryQueryResult> records = new ArrayList<>();
        try {
            for (KeyModification response : results) {
                log.info("response StringValue" + response.getStringValue());
                Fruit fruit;
                if (response.getStringValue() != null) {
                    fruit = JSON.parseObject(response.getStringValue(), Fruit.class);
                } else {
                    fruit = new Fruit();
                    fruit.setTraceability_code(traceability_code);
                }
                String timestamp = Instant.ofEpochSecond(response.getTimestamp().getEpochSecond())
                        .atZone(ZoneId.of("Asia/Shanghai"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                HistoryQueryResult record = new HistoryQueryResult(fruit, response.getTxId(), timestamp, response.isDeleted());
                records.add(record);
            }
        } catch (Exception e) {
            log.info("Failed to get fruit history: " + e);
            throw new ChaincodeException("Failed to get fruit history: " + e.getMessage());
        } finally {
            results.close();
        }
        return records.toArray(new HistoryQueryResult[records.size()]);

    }

    // 获取格式化的时间戳
    private String getFormattedTimestamp(ChaincodeStub stub) {
        return Instant.ofEpochSecond(stub.getTxTimestamp().getEpochSecond())
                .atZone(ZoneId.of("Asia/Shanghai"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public void beforeTransaction(Context ctx) {
        log.info("*************************************** beforeTransaction ***************************************");
    }

    @Override
    public void afterTransaction(Context ctx, Object result) {
        log.info("*************************************** afterTransaction ***************************************");
        log.info("result --------> " + result);
    }
}
