package com.rich;

import com.rich.boot.ManyGenerateApplication;
import com.rich.entity.CatTable;
import com.rich.entity.Config;
import com.rich.service.DataBaseService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author l_y
 * @version 1.0.0
 * @ClassName test.DataBaseTest.java
 * @Description TODO
 * @createTime 2022年02月27日 22:01:00
 */
@SpringBootTest(classes = ManyGenerateApplication.class)
public class DataBaseTest {

    @Autowired
    private DataBaseService dataBaseService;

    @Test
    void init(HttpServletResponse response) throws IOException {
        List<CatTable> tables = queryList();
        Config mysqlConfig = getDb();
        mysqlConfig.setTableNames(tables);
        dataBaseService.generateDbCode(mysqlConfig, response);
    }

    private Config getDb() {
        Config mysqlConfig = new Config();
        mysqlConfig.setIp("192.168.0.222");
        mysqlConfig.setCodePackage("com.khidi");
        mysqlConfig.setPort("3306");
        mysqlConfig.setUserName("root");
        mysqlConfig.setPwd("root");
        mysqlConfig.setStoreName("ybt_smart_casting");
        mysqlConfig.setAddress("C:\\Downloads");
        mysqlConfig.setDbType("MYSQL");
        return mysqlConfig;
    }

    @Test
    List<CatTable> queryList() {
        return dataBaseService.generateTableColShow(getDb());
    }


}
