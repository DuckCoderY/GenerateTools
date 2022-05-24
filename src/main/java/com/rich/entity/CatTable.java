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
public class CatTable {
    private String tableName;
    private String tableComment;

}
