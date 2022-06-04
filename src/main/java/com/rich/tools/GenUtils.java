package com.rich.tools;

import com.rich.entity.*;
import com.rich.utils.DateUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName: GenUtils
 * @Date: 2022/4/22 16:27
 * @Author: l_y
 * @Version: 1.0
 */

public class GenUtils {


    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("templates/bean/Entity.java.vm");

        templates.add("templates/bean/Dao.xml.vm");
        templates.add("templates/bean/Dao.java.vm");
        templates.add("templates/bean/ResponseBean.java.vm");
        templates.add("templates/bean/DataConvertUtils.java.vm");

        templates.add("templates/bean/Controller.java.vm");
//        templates.add("templates/bean/Service.java.vm");
        templates.add("templates/bean/Service.java.vm");


        templates.add("templates/bean/Api.js.vm");
        templates.add("templates/bean/request.js.vm");

        return templates;
    }


    public static void generatorCode(CatTable table, List<CatColumn> columns, ZipOutputStream zip, Config configH5, BooleanEntity booleanEntity) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        boolean hasList = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.getTableName());
        tableEntity.setComments(StringUtils.isEmpty(table.getTableComment()) ? " " : table.getTableComment());
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), new String[]{configH5.getTablePrefix()});
        tableEntity.setClassName(className);
        tableEntity.setClassNameLow(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (CatColumn column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.getColumnName());
            columnEntity.setDataType(column.getDataType());
            columnEntity.setComments(StringUtils.isEmpty(column.getColumnComment()) ? " " : column.getColumnComment());
            columnEntity.setExtra(column.getExtra());

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrNameLow(StringUtils.uncapitalize(attrName));
            columnEntity.setAttrNameUp(column.getColumnName().toUpperCase());

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), columnToJava(columnEntity.getDataType()));
            columnEntity.setAttrType(attrType);


            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            if (!hasList && "array".equals(columnEntity.getExtra())) {
                hasList = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.getColumnKey()) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class" , "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = configH5.getAddress();
        mainPath = StringUtils.isBlank(mainPath) ? "com.rich" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName" , tableEntity.getTableName());
        map.put("comments" , tableEntity.getComments());
        map.put("pk" , tableEntity.getPk());
        map.put("className" , tableEntity.getClassName());
        map.put("classNameLow" , tableEntity.getClassNameLow());
        map.put("pathName" , tableEntity.getClassNameLow().toLowerCase());
        map.put("columns" , tableEntity.getColumns());
        map.put("hasBigDecimal" , hasBigDecimal);
        map.put("hasList" , hasList);
        map.put("mainPath" , mainPath);
        map.put("package" , configH5.getCodePackage());
        map.put("moduleName" , StringUtils.isEmpty(configH5.getModelName()) ? "" : "." + configH5.getModelName());
        map.put("author" , config.getString("author"));
        map.put("email" , config.getString("email"));
        map.put("datetime" , DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);


        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            if ("templates/bean/ResponseBean.java.vm".equals(template) && booleanEntity.getBeenBoolean().get()) {
                continue;
            }
            if ("templates/bean/ResponseBean.java.vm".equals(template) && !booleanEntity.getBeenBoolean().get()) {
                booleanEntity.getBeenBoolean().set(true);
            }

            if ("templates/bean/DataConvertUtils.java.vm".equals(template) && booleanEntity.getConvertBoolean().get()) {
                continue;
            }
            if ("templates/bean/DataConvertUtils.java.vm".equals(template) && !booleanEntity.getConvertBoolean().get()) {
                booleanEntity.getConvertBoolean().set(true);
            }

            if ("templates/bean/request.js.vm".equals(template) && booleanEntity.getRequestBoolean().get()) {
                continue;
            }
            if ("templates/bean/request.js.vm".equals(template) && !booleanEntity.getRequestBoolean().get()) {
                booleanEntity.getRequestBoolean().set(true);
            }

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), configH5.getCodePackage(), configH5.getModelName() == null ? "" : configH5.getModelName())));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_" , "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String[] tablePrefixArray) {
        if (null != tablePrefixArray && tablePrefixArray.length > 0) {
            for (String tablePrefix : tablePrefixArray) {
                if (tableName.startsWith(tablePrefix)) {
                    tableName = tableName.replaceFirst(tablePrefix, "");
                }
            }
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace("." , File.separator) + File.separator + moduleName + File.separator;
        }

        if (template.contains("Entity.java.vm") || template.contains("MongoEntity.java.vm")) {
            return packagePath + "entity" + File.separator + className + "Entity.java" ;
        }

        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java" ;
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java" ;
        }

//        if (template.contains("Service.java.vm")) {
//            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java" ;
//        }

        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java" ;
        }

        if (template.contains("ResponseBean.java.vm")) {
            return packagePath + "utils" + File.separator + "ResponseBean.java" ;
        }


        if (template.contains("DataConvertUtils.java.vm")) {
            return packagePath + "utils" + File.separator + "DataConvertUtils.java" ;
        }

        if (template.contains("Dao.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Dao.xml" ;
        }

        if (template.contains("request.js.vm")) {
            return "main" + File.separator + "vue" + File.separator + "request.js" ;
        }

        if (template.contains("Api.js.vm")) {
            return "main" + File.separator + "vue" + File.separator + "api" + File.separator + StringUtils.uncapitalize(className) + "Api.js" ;
        }

        return null;
    }

    private static String splitInnerName(String name) {
        name = name.replaceAll("\\." , "_");
        return name;
    }


}
