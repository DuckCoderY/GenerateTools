package com.rich.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CatColumn
 * @Description
 * @Author jimi.zhang
 * @Date 2019/8/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatColumn {
    private String tableName;
    private String columnName;
    private String dataType;
    private String columnComment;
    private String extra;
    private String columnKey;
}
