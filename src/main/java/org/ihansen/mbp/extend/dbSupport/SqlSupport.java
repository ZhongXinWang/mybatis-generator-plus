package org.ihansen.mbp.extend.dbSupport;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
/**
* 数据库支持接口
* @author Winston.Wang
* @date 2019/8/4
* @version 1.0
**/
public interface SqlSupport {
    /**
     * 向&lt;mapper&gt;中添加子节点
     * @parameter @param document
     * @parameter @param introspectedTable
     */
    void sqlDialect(Document document, IntrospectedTable introspectedTable);

    /**
     * 增加批量插入的xml配置
     * @parameter @param document
     * @parameter @param introspectedTable
     */
    void addBatchInsertXml(Document document, IntrospectedTable introspectedTable);

    /**
     * 条件查询sql适配
     * @parameter @param element
     * @parameter @param introspectedTable06
     * @return
     */
    XmlElement adaptSelectByExample(XmlElement element, IntrospectedTable introspectedTable);

    /**
     * 插入sql适配
     * @parameter @param element
     * @parameter @param introspectedTable
     */
    void adaptInsertSelective(XmlElement element, IntrospectedTable introspectedTable);

    /**
     * 大偏移批量查询Mapper方法
     * @param interfaze
     * @param introspectedTable
     */
    void addSelectByBigOffsetMethod(Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * 大偏移批量查询Mapper.xml方法
     * @param document
     * @param introspectedTable
     */
    void addSelectByBigOffsetXml(Document document, IntrospectedTable introspectedTable);

    /**
     * 乐观锁更新
     * @param interfaze
     * @param introspectedTable
     */
    void addUpdateByOptimisticLockMethod(Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * 乐观锁XML
     * @param document
     * @param introspectedTable
     */
    void addUpdateByOptimisticLockXML(Document document, IntrospectedTable introspectedTable);
}
