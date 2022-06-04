package com.rich.tools;

import com.rich.entity.CatTable;
import com.rich.entity.Config;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName: BaseTool
 * @Date: 2022/4/22 15:40
 * @Author: l_y
 * @Version: 1.0
 */

public interface BaseTool {

    /**
     * TODO 获取所有表名 备注
     *
     * @param config:
     * @Author: l_y
     * @Date: 2022/4/22 15:42
     * @return: java.util.List<com.rich.entity.CatTable>
     **/
    List<CatTable> getAllTables(Config config);


    /**
     * TODO 生成代码
     *
     * @param config:
     * @param response:
     * @Author: l_y
     * @Date: 2022/4/22 15:56
     * @return: void
     **/
    void generateCode(Config config, HttpServletResponse response);

}
