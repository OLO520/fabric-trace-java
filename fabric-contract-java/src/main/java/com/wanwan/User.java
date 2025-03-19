package com.wanwan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;
import java.util.List;

// 用户结构体
@DataType
@Data
@Accessors(chain = true)
@NoArgsConstructor  // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
public class User {
    @Property
    private String userID;
    @Property
    private String userType;
    @Property
    private String realInfoHash;
    @Property
    private List<Fruit> fruitList;


    public User(@JsonProperty("userID")String userID,@JsonProperty("userType") String userType,@JsonProperty("realInfoHash") String realInfoHash) {
        this.userID = userID;
        this.userType = userType;
        this.realInfoHash = realInfoHash;
        this.fruitList = new ArrayList<>();
    }
    public void addFruit(Fruit fruit) {
        this.fruitList.add(fruit);
    }

}













