package com.wanwan;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

// 种植户输入信息结构体
@Data
@DataType
@NoArgsConstructor  // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
@Accessors(chain = true)
public class FarmerInput {
    @Property
    private String fa_fruitName;
    @Property
    private String fa_origin;
    @Property
    private String fa_plantTime;
    @Property
    private String fa_pickingTime;
    @Property
    private String fa_farmerName;
    @Property
    private String fa_txid;
    @Property
    private String fa_timestamp;

}
