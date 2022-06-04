package com.rich.tools;

import com.rich.entity.BooleanEntity;
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
import java.util.zip.ZipOutputStream;

/**
 * @ClassName: DbPostgreSqlWriteTool
 * @Date: 2022/4/25 11:21
 * @Author: l_y
 * @Version: 1.0
 */
@Service
public class DbPostgreSqlWriteTool implements BaseTool {


    @Override
    public List<CatTable> getAllTables(Config config) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            List<CatTable> tables = new ArrayList<>();
            Connection connection = getConnectionByConfig(config);
            String sql = " select t1.tablename as tableName, obj_description(relfilenode, 'pg_class') as tableComment, now() as createTime from pg_tables t1, pg_class t2\n" +
                    " where t1.tablename not like 'pg%' and t1.tablename not like 'sql_%' and t1.tablename = t2.relname\n" +
                    " order by t1.tablename desc " ;
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tables.add(new CatTable(resultSet.getString("TableName"), resultSet.getString("TableComment")));
            }
            return tables;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void generateCode(Config config, HttpServletResponse response) {
        byte[] data = generatorCode(config);
        response.reset();
        response.setHeader("Content-Disposition" , "attachment; filename=\"" + config.getStoreName() + ".zip\"");
        response.addHeader("Content-Length" , "" + data.length);
        response.addHeader("Access-Control-Allow-Origin" , "*");
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
        BooleanEntity booleanEntity = new BooleanEntity();
        for (CatTable table : config.getTableNames()) {
            //查询列信息
            List<CatColumn> columns = queryColumns(table.getTableName(), config);
            //生成代码
            GenUtils.generatorCode(table, columns, zip, config, booleanEntity);
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
            String sql = "select t2.attname as columnName, pg_type.typname as dataType, col_description(t2.attrelid,t2.attnum) as columnComment, '' as extra,\n" +
                    "(CASE t3.contype WHEN 'p' THEN 'PRI' ELSE '' END) as columnKey\n" +
                    "from pg_class as t1, pg_attribute as t2 inner join pg_type on pg_type.oid = t2.atttypid\n" +
                    "left join pg_constraint t3 on t2.attnum = t3.conkey[1] and t2.attrelid = t3.conrelid\n" +
                    "where t1.relname = ? and t2.attrelid = t1.oid and t2.attnum>0" ;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tableName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tables.add(new CatColumn(tableName, resultSet.getString("ColumnName"),
                        resultSet.getString("DataType"), resultSet.getString("ColumnComment"),
                        resultSet.getString("EXTRA"), resultSet.getString("ColumnKey")));
            }
            return tables;
        } catch (Exception e) {
            return tables;
        }
    }


    private Connection getConnectionByConfig(Config config) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://" + config.getIp() + ":" + config.getPort() + "/" + config.getStoreName() + "" ,
                config.getUserName(), config.getPwd());
    }

}
