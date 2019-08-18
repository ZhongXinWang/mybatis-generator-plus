package org.ihansen.mbp.extend.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.TableConfiguration;

import java.util.List;
import java.util.Properties;

/**
* 生成乐观锁Xml
* @author Winston.Wang
* @date 2019/8/6
* @version 1.0
**/
public class UpdateByOptimisticLockMysqlElementGenerator implements AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
        Properties properties = tableConfiguration.getProperties();
        String versionField = properties.getProperty("versionField");
        if (versionField == null || versionField == "") {
            return;
        }
        XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", UpdateByOptimisticLockMethodGenerator.METHOD_NAME));
        String parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }
        answer.addAttribute(new Attribute("parameterType", parameterType));
        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        XmlElement dynamicElement = new XmlElement("set");
        answer.addElement(dynamicElement);
        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
            XmlElement isNotNullElement = new XmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement.addElement(isNotNullElement);
            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append(',');
            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        //version = version+1,
        dynamicElement.addElement(new TextElement(String.format("%s = %s+1,",versionField,versionField)));

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and ");
            } else {
                sb.append("where ");
                and = true;
            }
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getBaseColumns();
        IntrospectedColumn versionIntrospectedColumn = null;
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (versionField.equals(introspectedColumn.getActualColumnName())){
                versionIntrospectedColumn = introspectedColumn;
            }
        }
        //and version = #{id,jdbcType=INTEGER}
        answer.addElement(new TextElement(String.format("and %s = #{%s,jdbcType=%s}", versionField, versionIntrospectedColumn.getJavaProperty(), versionIntrospectedColumn.getJdbcTypeName())));
        parentElement.addElement(answer);
    }
}
