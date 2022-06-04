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
 * @ClassName: DbSqlServerWriteTool
 * @Date: 2022/4/25 10:16
 * @Author: l_y
 * @Version: 1.0
 */
@Service
public class DbSqlServerWriteTool implements BaseTool {

    @Override
    public List<CatTable> getAllTables(Config config) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            List<CatTable> tables = new ArrayList<>();
            Connection connection = getConnectionByConfig(config);
            String sql = " select * from (" +
                    " select cast(so.name as nvarchar(500)) as tableName, cast(sep.value as nvarchar(500)) as tableComment, getDate() as createTime\n" +
                    " from sysobjects so\n" +
                    " left JOIN sys.extended_properties sep\n" +
                    " on sep.major_id=so.id and sep.minor_id=0\n" +
                    " where (xtype='U' or xtype='v')\n" +
                    " ) t where 1=1\n" +
                    " order by t.tableName" ;
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tables.add(new CatTable(resultSet.getString("tableName"),
                        resultSet.getString("tableComment")));
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
            String sql = "SELECT\n" +
                    "cast(b.NAME AS NVARCHAR(500)) AS columnName,cast(sys.types.NAME AS NVARCHAR(500)) AS dataType,cast(\tc.VALUE AS NVARCHAR(500)) AS columnComment,\n" +
                    "( SELECT CASE count( 1 )\tWHEN 1 then 'PRI'\tELSE ''\tEND\tFROM syscolumns,sysobjects,sysindexes,sysindexkeys,systypes\n" +
                    " WHERE\tsyscolumns.xusertype = systypes.xusertype\n" +
                    " AND syscolumns.id = object_id(A.NAME)\n" +
                    " AND sysobjects.xtype = 'PK'\n" +
                    " AND sysobjects.parent_obj = syscolumns.id\n" +
                    " AND sysindexes.id = syscolumns.id\n" +
                    " AND sysobjects.NAME = sysindexes.NAME\n" +
                    " AND sysindexkeys.id = syscolumns.id\n" +
                    " AND sysindexkeys.indid = sysindexes.indid\n" +
                    " AND syscolumns.colid = sysindexkeys.colid\n" +
                    " AND syscolumns.NAME = B.NAME\n" +
                    " ) as columnKey,\n" +
                    " '' as extra\n" +
                    " FROM (select name,object_id from sys.tables UNION all select name, object_id from\tsys.views\t) a\n" +
                    " INNER JOIN sys.COLUMNS b ON\n" +
                    " b.object_id = a.object_id\n" +
                    " LEFT JOIN sys.types ON\n" +
                    " b.user_type_id = sys.types.user_type_id\n" +
                    " LEFT JOIN sys.extended_properties c ON\n" +
                    " c.major_id = b.object_id\n" +
                    " AND c.minor_id = b.column_id\n" +
                    " WHERE\n" +
                    " a.NAME = ? \n" +
                    " and sys.types.NAME != 'sysname' " ;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tableName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tables.add(new CatColumn(tableName, resultSet.getString("columnName"),
                        resultSet.getString("dataType"), resultSet.getString("columnComment"),
                        resultSet.getString("extra"), resultSet.getString("columnKey")));
            }
            return tables;
        } catch (Exception e) {
            return tables;
        }
    }


    private Connection getConnectionByConfig(Config config) throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(
                "jdbc:sqlserver://" + config.getIp() + ":" + config.getPort() + ";DataBaseName=" + config.getStoreName() + "" ,
                config.getUserName(), config.getPwd());
    }

}
