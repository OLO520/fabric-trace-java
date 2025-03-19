package com.wanwan;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

// 工厂输入信息结构体
@Data
@DataType
@NoArgsConstructor  // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
@Accessors(chain = true)
public class FactoryInput {
    @Property
    private String fac_productName;
    @Property
    private String fac_productionbatch;
    @Property
    private String fac_productionTime;
    @Property
    private String fac_factoryName;
    @Property
    private String fac_contactNumber;
    @Property
    private String fac_txid;
    @Property
    private String fac_timestamp;

}
