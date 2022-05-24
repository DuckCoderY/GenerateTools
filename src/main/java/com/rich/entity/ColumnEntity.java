package com.rich.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: l_y
 * @Date: 2022/4/22 13:40
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnEntity {
	//列名
    private String columnName;
    //列名类型
    private String dataType;
    //列名备注
    private String comments;
    
    //属性名称(第一个字母大写)，如：user_name => UserName
    private String attrName;
    //属性名称(第一个字母小写)，如：user_name => userName
    private String attrNameLow;
    //全大写
    private String attrNameUp;
    //属性类型
    private String attrType;
    //auto_increment
    private String extra;

}
