package com.rich.service;

import com.rich.entity.CatTable;
import com.rich.entity.Config;
import com.rich.tools.DbMysqlWriteTool;
import com.rich.tools.DbOracleWriteTool;
import com.rich.tools.DbPostgreSqlWriteTool;
import com.rich.tools.DbSqlServerWriteTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @Author: l_y
 * @Date: 2022/4/22 13:40
 **/

@Service
public class DataBaseService {
    private static final String MYSQL = "MYSQL";
    private static final String ORACLE = "ORACLE";
    private static final String SQLSERVER = "SQLSERVER";
    private static final String PGSQL = "PGSQL";

    @Autowired
    private DbMysqlWriteTool dbMysqlWriteTool;
    @Autowired
    private DbOracleWriteTool dbOracleWriteTool;
    @Autowired
    private DbSqlServerWriteTool dbSqlServerWriteTool;
    @Autowired
    private DbPostgreSqlWriteTool dbPostgreSqlWriteTool;

    public List<CatTable> generateTableColShow(Config config) {
        switch (config.getDbType()) {
            case MYSQL:
                return dbMysqlWriteTool.getAllTables(config);
            case ORACLE:
                return dbOracleWriteTool.getAllTables(config);
            case SQLSERVER:
                return dbSqlServerWriteTool.getAllTables(config);
            case PGSQL:
                return dbPostgreSqlWriteTool.getAllTables(config);
            default:
                return Collections.EMPTY_LIST;
        }
    }

    public void generateDbCode(Config config, HttpServletResponse response) {
        switch (config.getDbType()) {
            case MYSQL:
                dbMysqlWriteTool.generateCode(config, response);
                return;
            case ORACLE:
                dbOracleWriteTool.generateCode(config, response);
                return;
            case SQLSERVER:
                dbSqlServerWriteTool.generateCode(config, response);
                return;
            case PGSQL:
                dbPostgreSqlWriteTool.generateCode(config, response);
                return;
            default:
        }

    }

}
