package org.ihansen.mbp.extend.dbSupport;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.config.TableConfiguration;

import java.util.List;
import java.util.Properties;
/**
* oracle数据库支持
* @author Winston.Wang
* @date 2019/8/4
* @version 1.0
**/
public class OracleSupport implements SqlSupport {
    /**
     * 向&lt;mapper&gt;中子节点中添加支持批量和分页查询的sql代码块
     * @parameter @param document
     * @parameter @param introspectedTable
     */
    @Override
    public void sqlDialect(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        // 1.产生分页语句前半部分
        XmlElement paginationPrefixElement = new XmlElement("sql");
        paginationPrefixElement.addAttribute(new Attribute("id", "OracleDialectPrefix"));
        XmlElement pageStart = new XmlElement("if");
        pageStart.addAttribute(new Attribute("test", "offset != null and end != null"));
        pageStart.addElement(new TextElement("select * from ( select row_.*, rownum rownum_ from ( "));
        paginationPrefixElement.addElement(pageStart);
        parentElement.addElement(paginationPrefixElement);

        // 2.产生分页语句后半部分
        XmlElement paginationSuffixElement = new XmlElement("sql");
        paginationSuffixElement.addAttribute(new Attribute("id", "OracleDialectSuffix"));
        XmlElement pageEnd = new XmlElement("if");
        pageEnd.addAttribute(new Attribute("test", "offset != null and end != null"));
        pageEnd.addElement(new TextElement("<![CDATA[ ) row_  where rownum <= #{end} ) where rownum_ > #{offset}]]>"));
        paginationSuffixElement.addElement(pageEnd);
        parentElement.addElement(paginationSuffixElement);

        // 3.获取序列的xml配置
        XmlElement tableSequence = new XmlElement("sql");
        tableSequence.addAttribute(new Attribute("id", "TABLE_SEQUENCE"));
        String tableSequenceNextval = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime() + "_SEQUENCE.nextval";
        tableSequence.addElement(new TextElement(tableSequenceNextval));
        parentElement.addElement(tableSequence);
    }

    /**
     * 生成批量插入的动态sql代码
     * @parameter @param document
     * @parameter @param introspectedTable
     */
    @Override
    public void addBatchInsertXml(Document document, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        //获得要自增的列名
        String incrementField = introspectedTable.getTableConfiguration().getProperties().getProperty("incrementField");
        if (incrementField != null) {
            incrementField = incrementField.toUpperCase();
        }

        StringBuilder dbColumnsName = new StringBuilder();
        StringBuilder javaPropertyAndDbType = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : columns) {
            String columnName = introspectedColumn.getActualColumnName();
            dbColumnsName.append(columnName + ",");
            // 不设置id
            if (!columnName.toUpperCase().equals(incrementField)) {
                javaPropertyAndDbType.append("#{item." + introspectedColumn.getJavaProperty() + ",jdbcType=" + introspectedColumn.getJdbcTypeName() + "},");
            }
        }

        XmlElement insertBatchElement = new XmlElement("insert");
        insertBatchElement.addAttribute(new Attribute("id", "insertBatch"));
        insertBatchElement.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        insertBatchElement.addElement(new TextElement("insert into " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));

        XmlElement trim1Element = new XmlElement("trim");
        trim1Element.addAttribute(new Attribute("prefix", "("));
        trim1Element.addAttribute(new Attribute("suffix", ")"));
        trim1Element.addAttribute(new Attribute("suffixOverrides", ","));
        trim1Element.addElement(new TextElement(dbColumnsName.toString()));
        insertBatchElement.addElement(trim1Element);

        insertBatchElement.addElement(new TextElement("select " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime() + "_SEQUENCE.nextval,A.* from("));

        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("index", "index"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", "UNION ALL"));
        foreachElement.addElement(new TextElement("SELECT"));
        XmlElement trim2Element = new XmlElement("trim");
        trim2Element.addAttribute(new Attribute("suffixOverrides", ","));
        trim2Element.addElement(new TextElement(javaPropertyAndDbType.toString()));
        foreachElement.addElement(trim2Element);
        foreachElement.addElement(new TextElement("from dual"));
        insertBatchElement.addElement(foreachElement);

        insertBatchElement.addElement(new TextElement(") A"));

        document.getRootElement().addElement(insertBatchElement);
    }

    /**
     * 向查询节点中添加分页支持
     * @parameter @param element
     * @parameter @param preFixId
     * @parameter @param sufFixId
     */
    @Override
    public XmlElement adaptSelectByExample(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement pageStart = new XmlElement("include");
        pageStart.addAttribute(new Attribute("refid", "OracleDialectPrefix"));
        element.getElements().add(0, pageStart);
        XmlElement isNotNullElement = new XmlElement("include");
        isNotNullElement.addAttribute(new Attribute("refid", "OracleDialectSuffix"));
        element.getElements().add(isNotNullElement);
        return element;
    }

    /**
     * 在单条插入动态sql中增加查询序列，以实现oracle主键自增
     * @parameter @param element
     * @parameter @param introspectedTable
     */
    @Override
    public void adaptInsertSelective(XmlElement element, IntrospectedTable introspectedTable) {
        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
        Properties properties = tableConfiguration.getProperties();
        String incrementFieldName = properties.getProperty("incrementField");
        // 有自增字段的配置
        if (incrementFieldName != null) {
            List<Element> elements = element.getElements();
            XmlElement selectKey = new XmlElement("selectKey");
            selectKey.addAttribute(new Attribute("keyProperty", incrementFieldName));
            selectKey.addAttribute(new Attribute("resultType", "java.lang.Long"));
            selectKey.addAttribute(new Attribute("order", "BEFORE"));
            selectKey.addElement(new TextElement("select "));
            XmlElement includeSeq = new XmlElement("include");
            includeSeq.addAttribute(new Attribute("refid", "TABLE_SEQUENCE"));
            selectKey.addElement(includeSeq);
            selectKey.addElement(new TextElement(" from dual"));
            elements.add(0, selectKey);
        }
    }

    @Override
    public void addSelectByBigOffsetMethod(Interface interfaze, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addSelectByBigOffsetXml(Document document, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addUpdateByOptimisticLockMethod(Interface interfaze, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addUpdateByOptimisticLockXML(Document document, IntrospectedTable introspectedTable) {

    }
}
