package com.rich.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CatTable
 * @Description
 * @Author jimi.zhang
 * @Date 2019/8/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatTable {
    private String tableName;
    private String tableComment;

}
