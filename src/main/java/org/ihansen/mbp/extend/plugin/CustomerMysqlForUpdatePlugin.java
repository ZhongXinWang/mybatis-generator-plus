package org.ihansen.mbp.extend.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;
/**
* Mysql添加forUpdate方法
* @author Winston.Wang
* @date 2019/8/8
* @version 1.0
**/
public class CustomerMysqlForUpdatePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 修改Example添加forUpdate属性字段
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        PrimitiveTypeWrapper booleanWrapper = FullyQualifiedJavaType.getBooleanPrimitiveInstance().getPrimitiveTypeWrapper();
        Field forUpdate = new Field();
        forUpdate.setName("forUpdate");
        forUpdate.setVisibility(JavaVisibility.PRIVATE);
        forUpdate.setType(booleanWrapper);
        topLevelClass.addField(forUpdate);

        Method setForUpdate = new Method();
        setForUpdate.setVisibility(JavaVisibility.PUBLIC);
        setForUpdate.setName("setForUpdate");
        setForUpdate.addParameter(new Parameter(booleanWrapper, "forUpdate"));
        setForUpdate.addBodyLine("this.forUpdate = forUpdate;");
        topLevelClass.addMethod(setForUpdate);

        Method getForUpdate = new Method();
        getForUpdate.setVisibility(JavaVisibility.PUBLIC);
        getForUpdate.setReturnType(booleanWrapper);
        getForUpdate.setName("getForUpdate");
        getForUpdate.addBodyLine("return forUpdate;");
        topLevelClass.addMethod(getForUpdate);

        return true;
    }

    /**
     * 给select添加forUpdate的xml
     * @param element
     * @param introspectedTable
     */
    private void appendForUpdate(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement forUpdateElement = new XmlElement("if");
        forUpdateElement.addAttribute(new Attribute("test", "forUpdate != null and forUpdate == true"));
        forUpdateElement.addElement(new TextElement("for update"));
        element.addElement(forUpdateElement);
    }


    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        appendForUpdate(element, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        appendForUpdate(element, introspectedTable);
        return true;
    }
}
