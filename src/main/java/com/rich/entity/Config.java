package com.rich.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: MysqlConfig
 * @Date: 2021/12/12 20:45
 * @Author: Clown B
 * @Version: 1.0
 */
@Data
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
