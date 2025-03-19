package com.wanwan;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

// 商店输入信息结构体
@Data
@DataType
@NoArgsConstructor  // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
@Accessors(chain = true)
public class ShopInput {
    @Property
    private String sh_storeTime;
    @Property
    private String sh_sellTime;
    @Property
    private String sh_shopName;
    @Property
    private String sh_shopAddress;
    @Property
    private String sh_shopPhone;
    @Property
    private String sh_txid;
    @Property
    private String sh_timestamp;

}
