package com.wanwan;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

// 运输司机输入信息结构体
@Data
@DataType
@NoArgsConstructor  // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
@Accessors(chain = true)
public class DriverInput {
    @Property
    private String dr_name;
    @Property
    private String dr_age;
    @Property
    private String dr_phone;
    @Property
    private String dr_carNumber;
    @Property
    private String dr_transport;
    @Property
    private String dr_txid;
    @Property
    private String dr_timestamp;

}
