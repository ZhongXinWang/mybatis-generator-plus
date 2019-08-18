package demo;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *mybatis 通过配置文件注入
* @author Winston.Wang
* @date 2019/7/28
* @version 1.0
**/
public class Main {

    public static void main(String[] args) {

        Configuration configuration = new Configuration();

        List<Context> contexts = createContext();

        configuration.addContext(contexts.get(0));
        MyBatisGenerator myBatisGenerator = null;

        try {

            myBatisGenerator  = new MyBatisGenerator(configuration,null,null);

        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            myBatisGenerator.generate(new ProgressCallback(){

                @Override
                public void introspectionStarted(int totalTasks) {

                    System.out.println(" introspectionStarted totalTasks:"+totalTasks);
                }

                @Override
                public void generationStarted(int totalTasks) {

                    System.out.println(" generationStarted totalTasks:"+totalTasks);
                }

                @Override
                public void saveStarted(int totalTasks) {

                    System.out.println("totalTasks:"+totalTasks);
                }

                @Override
                public void startTask(String taskName) {

                    System.out.println("taskName:"+taskName);
                }

                @Override
                public void done() {

                    System.out.println("done");
                }

                @Override
                public void checkCancel() throws InterruptedException {

                    System.out.println("checkCancel");
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static List<Context> createContext() {

        List<Context> list = new ArrayList<>();
        //创建第一个Context
        Context context = new Context(ModelType.CONDITIONAL);
        context.setTargetRuntime("MyBatis3");
        context.setId("myContextId");
        //设置备注生成器
        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        commentGeneratorConfiguration.setConfigurationType("org.ihansen.mbp.extend.generator.DbRemarksCommentGenerator");
        commentGeneratorConfiguration.addProperty("type","org.ihansen.mbp.extend.generator.DbRemarksCommentGenerator");
        //获取数据库列备注
        commentGeneratorConfiguration.addProperty("columnRemarks","true");
        //使用注解
        commentGeneratorConfiguration.addProperty("annotations","true");
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

        //创建数据库连接
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setDriverClass("com.mysql.jdbc.Driver");
        jdbcConnectionConfiguration.setConnectionURL("jdbc:mysql://127.0.0.1:3306/winston?characterEncoding=utf-8");
        jdbcConnectionConfiguration.setUserId("root");
        jdbcConnectionConfiguration.setPassword("mysqladmin");
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        /* 数据表对应的实体层*/
        JavaModelGeneratorConfiguration javaModelGenerator = new JavaModelGeneratorConfiguration();
        javaModelGenerator.setTargetPackage("demo.domain");
        javaModelGenerator.setTargetProject("src/test/java");
        context.setJavaModelGeneratorConfiguration(javaModelGenerator);

        /**
         * sql Mapper配置文件生成配置
         */
        SqlMapGeneratorConfiguration sqlMapGenerator = new SqlMapGeneratorConfiguration();
        sqlMapGenerator.setTargetProject("src/test/resources");
        sqlMapGenerator.setTargetPackage("demo.mapper.xml");
        sqlMapGenerator.addProperty("enableSubPackages","true");
        sqlMapGenerator.addProperty("isMergeable","true");
        context.setSqlMapGeneratorConfiguration(sqlMapGenerator);

        /**
         * 生成Mapper接口
         */
        JavaClientGeneratorConfiguration javaClientGenerator = new JavaClientGeneratorConfiguration();
        javaClientGenerator.setTargetPackage("demo.mapper");
        javaClientGenerator.setTargetProject("src/test/java");
        javaClientGenerator.setConfigurationType("XMLMAPPER");
        javaClientGenerator.addProperty("enableSubPackages","true");
        context.setJavaClientGeneratorConfiguration(javaClientGenerator);

        /**
         * 表属性配置
         */
        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName("b_book");
        tableConfiguration.setDomainObjectName("book.BookModel");
        tableConfiguration.setCountByExampleStatementEnabled(false);
        tableConfiguration.setSelectByExampleStatementEnabled(false);
        tableConfiguration.setDeleteByPrimaryKeyStatementEnabled(false);

        context.addTableConfiguration(tableConfiguration);


        TableConfiguration tableConfiguration1 = new TableConfiguration(context);
        tableConfiguration1.setTableName("b_document");
        tableConfiguration1.setDomainObjectName("book.DocumentModel");
        tableConfiguration1.setCountByExampleStatementEnabled(true);
        tableConfiguration1.setSelectByExampleStatementEnabled(true);
        tableConfiguration1.setDeleteByPrimaryKeyStatementEnabled(true);

        context.addTableConfiguration(tableConfiguration1);

        //设置插件
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        //添加实体序列化插件
        pluginConfiguration.addProperty("type","org.ihansen.mbp.extend.plugin.CustomSerializablePlugin");
        pluginConfiguration.setConfigurationType("org.ihansen.mbp.extend.plugin.CustomSerializablePlugin");
        context.addPluginConfiguration(pluginConfiguration);

        PluginConfiguration pluginConfiguration1 = new PluginConfiguration();
        pluginConfiguration1.setConfigurationType("org.ihansen.mbp.extend.CustomPlugin");
        pluginConfiguration1.addProperty("dbType","MYSQL");
//        context.addPluginConfiguration(pluginConfiguration1);

        PluginConfiguration pluginConfiguration2 = new PluginConfiguration();
        pluginConfiguration2.setConfigurationType("org.ihansen.mbp.extend.plugin.CustomerDaoInterfacePlugin");
        context.addPluginConfiguration(pluginConfiguration2);

        list.add(context);

        return list;
    }


}
