package org.ihansen.mbp.extend.plugin;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
* Example类添加分页属性
* @author Winston.Wang
* @date 2019/8/5
* @version 1.0
**/
public class CustomerExampleAddPagePlugin  extends  CustomerPluginAdapter{

    public  CustomerExampleAddPagePlugin(){}

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addPage(topLevelClass, introspectedTable);
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 修改Example类,添加分页支持
     * @param topLevelClass
     * @param introspectedTable
     */
    private void addPage(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //获取备注生成器
        CommentGenerator commentGenerator = context.getCommentGenerator();
        //添加 偏移量(offset)字段
        Field offsetField = new Field();
        offsetField.setVisibility(JavaVisibility.PROTECTED);
        offsetField.setType(new FullyQualifiedJavaType("java.lang.Long"));
        offsetField.setName("offset");
        commentGenerator.addFieldComment(offsetField, introspectedTable);
        topLevelClass.addField(offsetField);

        //添加 limit 字段
        Field limitField = new Field();
        limitField.setVisibility(JavaVisibility.PROTECTED);
        limitField.setType(new FullyQualifiedJavaType("java.lang.Long"));
        limitField.setName("limit");
        //使用默认的注释
        commentGenerator.addFieldComment(limitField, introspectedTable);
        topLevelClass.addField(limitField);

        //添加 end 字段
        Field endField = new Field();
        endField.setVisibility(JavaVisibility.PROTECTED);
        endField.setType(new FullyQualifiedJavaType("java.lang.Long"));
        endField.setName("end");
        commentGenerator.addFieldComment(endField, introspectedTable);
        topLevelClass.addField(endField);

        //添加 setPagination 设置分页的方法
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("setPagination");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("long"), "offset"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("long"), "limit"));
        method.addBodyLine("this.offset = offset;");
        method.addBodyLine("this.limit = limit;");
        method.addBodyLine("this.end = offset + limit;");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    /**
     * 修改Mapper.xml支持分页
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        //调用数据库Support
        getSqlSupport().sqlDialect(document, introspectedTable);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * SelectByExample 没有大blob字段的分页
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        //修改生成xml
        XmlElement newElement =getSqlSupport().adaptSelectByExample(element, introspectedTable);
        //调用父类方法
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(newElement, introspectedTable);
    }

    /**
     * 包含大字段的分页
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement newElement = getSqlSupport().adaptSelectByExample(element, introspectedTable);
        return super.sqlMapSelectByExampleWithBLOBsElementGenerated(newElement, introspectedTable);
    }
}
