package com.wanwan;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

// 农产品结构体
@DataType
@Data
@Accessors(chain = true)
@NoArgsConstructor  // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
public class Fruit {
    @Property
    private String traceability_code;
    @Property
    private FarmerInput farmer_input;
    @Property
    private FactoryInput factory_input;
    @Property
    private DriverInput driver_input;
    @Property
    private ShopInput shop_input;
}
