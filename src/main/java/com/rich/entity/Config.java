package com.rich.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: l_y
 * @Date: 2022/4/22 13:40
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {

    private String ip;
    private String port;
    private String storeName;
    private String userName;
    private String pwd;
    private String address;
    private List<CatTable> tableNames;
    private String codePackage;
    private String dbType;
    private String modelName;
    private String tablePrefix;

}
