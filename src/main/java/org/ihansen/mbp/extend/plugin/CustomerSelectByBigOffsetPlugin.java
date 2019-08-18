package org.ihansen.mbp.extend.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
/**
* 增加大偏移批量查询
* @author Winston.Wang
* @date 2019/8/6
* @version 1.0
**/
public class CustomerSelectByBigOffsetPlugin  extends  CustomerPluginAdapter{

    /**
     * 修改mapper大偏移批量查询方法
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        //增加大偏移批量查询
        getSqlSupport().addSelectByBigOffsetMethod(interfaze, introspectedTable);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    /**
     * 修改mapper.xml 大偏移批量查询
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        getSqlSupport().addBatchInsertXml(document, introspectedTable);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }
}
