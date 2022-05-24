package com.rich.tools;

import com.rich.entity.CatColumn;
import com.rich.entity.CatTable;
import com.rich.entity.Config;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName: DbMysqlWriteTool
 * @Date: 2022/4/22 15:32
 * @Author: l_y
 * @Version: 1.0
 */
@Service
public class DbMysqlWriteTool implements BaseTool {

    @Override
    public List<CatTable> getAllTables(Config config) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            List<CatTable> tables = new ArrayList<>();
            Connection connection = getConnectionByConfig(config);
            String sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, config.getStoreName());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tables.add(new CatTable(resultSet.getString("TABLE_NAME"), resultSet.getString("TABLE_COMMENT")));
            }
            return tables;
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void generateCode(Config config, HttpServletResponse response) {
        byte[] data = generatorCode(config);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\""+config.getStoreName()+".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/octet-stream; charset=UTF-8");
        try {
            IOUtils.write(data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] generatorCode(Config config) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        AtomicBoolean beenBoolean = new AtomicBoolean(false);
        AtomicBoolean convertBoolean = new AtomicBoolean(false);
        for (CatTable table : config.getTableNames()) {
            //查询列信息
            List<CatColumn> columns = queryColumns(table.getTableName(), config);
            //生成代码
            GenUtils.generatorCode(table, columns, zip, config,beenBoolean,convertBoolean);
        }

        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    private List<CatColumn> queryColumns(String tableName, Config config) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<CatColumn> tables = new ArrayList<>();
        try {
            Connection connection = getConnectionByConfig(config);
            String sql = "SELECT COLUMN_NAME , DATA_TYPE , COLUMN_COMMENT , COLUMN_KEY , EXTRA " +
                    " FROM INFORMATION_SCHEMA.COLUMNS " +
                    " WHERE TABLE_NAME = ? AND TABLE_SCHEMA = (SELECT DATABASE()) ORDER BY ORDINAL_POSITION ASC ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tableName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tables.add(new CatColumn(tableName, resultSet.getString("COLUMN_NAME"),
                        resultSet.getString("DATA_TYPE"), resultSet.getString("COLUMN_COMMENT"),
                        resultSet.getString("EXTRA"), resultSet.getString("COLUMN_KEY")));
            }
            return tables;
        } catch (Exception e) {
            return tables;
        }
    }


    private Connection getConnectionByConfig(Config config) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://" + config.getIp() + ":" + config.getPort() + "/" + config.getStoreName() + "" +
                        "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2b8&useSSL=false&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true&allowMultiQueries=true",
                config.getUserName(), config.getPwd());
    }
}
