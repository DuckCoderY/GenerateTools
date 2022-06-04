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
 * @ClassName: DbOracleWriteTool
 * @Date: 2022/4/25 10:02
 * @Author: l_y
 * @Version: 1.0
 */
@Service
public class DbOracleWriteTool implements BaseTool {
    @Override
    public List<CatTable> getAllTables(Config config) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            List<CatTable> tables = new ArrayList<>();
            Connection connection = getConnectionByConfig(config);
            String sql = " select dt.table_name tableName,\n" +
                    " dtc.comments tableComment,\n" +
                    " uo.created createTime\n" +
                    " from user_tables dt,\n" +
                    " user_tab_comments dtc,\n" +
                    " user_objects uo\n" +
                    " where dt.table_name = dtc.table_name and dt.table_name = uo.object_name and uo.object_type='TABLE'\n" +
                    " order by uo.CREATED desc" ;
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
            String sql = " select temp.column_name columnname,\n" +
                    " temp.data_type dataType,\n" +
                    " temp.comments columnComment,\n" +
                    " case temp.constraint_type when 'P' then 'PRI' when 'C' then 'UNI' else '' end \"COLUMNKEY\",\n" +
                    " '' \"EXTRA\"\n" +
                    " from (\n" +
                    " select col.column_id,\n" +
                    " col.column_name,\n" +
                    " col.data_type,\n" +
                    " colc.comments,\n" +
                    " uc.constraint_type,\n" +
                    " row_number() over (partition by col.column_name order by uc.constraint_type desc) as row_flg\n" +
                    " from user_tab_columns col\n" +
                    " left join user_col_comments colc\n" +
                    " on colc.table_name = col.table_name\n" +
                    " and colc.column_name = col.column_name\n" +
                    " left join user_cons_columns ucc\n" +
                    " on ucc.table_name = col.table_name\n" +
                    " and ucc.column_name = col.column_name\n" +
                    " left join user_constraints uc\n" +
                    " on uc.constraint_name = ucc.constraint_name\n" +
                    "   where col.table_name = upper( ? )\n" +
                    " ) temp\n" +
                    " where temp.row_flg = 1\n" +
                    " order by temp.column_id" ;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tableName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tables.add(new CatColumn(tableName, resultSet.getString("ColumnName"),
                        resultSet.getString("dataType"), resultSet.getString("ColumnComment"),
                        resultSet.getString("EXTRA"), resultSet.getString("ColumnKey")));
            }
            return tables;
        } catch (Exception e) {
            return tables;
        }
    }


    private Connection getConnectionByConfig(Config config) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.OracleDriver");
        return DriverManager.getConnection(
                "jdbc:oracle:thin:@" + config.getIp() + ":" + config.getPort() + ":" + config.getStoreName(),
                config.getUserName(), config.getPwd());
    }
}
