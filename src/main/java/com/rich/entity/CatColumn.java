package com.rich.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: l_y
 * @Date: 2022/4/22 13:40
 **/

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
