
package org.ihansen.mbp.extend.generator;

import org.ihansen.mbp.extend.util.CommentUtil;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
* 此插件使用数据库表中列的注释来生成Java Model中属性的注释
* @author Winston.Wang
* @date 2019/8/8
* @version 1.0
**/
public class DbRemarksCommentGenerator implements CommentGenerator {


    private Properties properties;
    private boolean columnRemarks;
    private boolean isAnnotations;
    /**
     * 是否是Model
     */
    private boolean isModel;

    public DbRemarksCommentGenerator() {
        super();
        properties = new Properties();
        isModel = false;
    }

    /**
     * 给java类添加注解
     * @param compilationUnit
     */
    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {

        CommentUtil.AddJavaDocToClass(compilationUnit,properties);

        //判断是否是Model结尾
        if(!compilationUnit.getType().getShortName().endsWith("Model")){
            isModel = false;
        }else{
            isModel = true;
        }
        //只给model类添加注解
        if (isAnnotations && isModel) {
            compilationUnit.addImportedType(new FullyQualifiedJavaType("javax.persistence.Table"));
            compilationUnit.addImportedType(new FullyQualifiedJavaType("javax.persistence.Id"));
            compilationUnit.addImportedType(new FullyQualifiedJavaType("javax.persistence.Column"));
            compilationUnit.addImportedType(new FullyQualifiedJavaType("javax.persistence.GeneratedValue"));
            compilationUnit.addImportedType(new FullyQualifiedJavaType("org.hibernate.validator.constraints.NotEmpty"));
        }
    }

    /**
     * 添加xml注释
     * @param xmlElement
     */
    @Override
    public void addComment(XmlElement xmlElement) {

    }

    @Override
    public void addRootComment(XmlElement rootElement) {

        return;
    }

    /**
     * 获取属性字段类型
     * @param properties
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        columnRemarks = isTrue(properties
                .getProperty("columnRemarks"));
        isAnnotations = isTrue(properties
                .getProperty("annotations"));
    }

    /**
     * Example注释
     * @param innerClass
     * @param introspectedTable
     */
    @Override
    public void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable) {

        StringBuilder sb = new StringBuilder();
        innerClass.addJavaDocLine("/**");
        innerClass
                .addJavaDocLine(" * 内部静态类添加注解");
        sb.append(" * This class corresponds to the database table ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(sb.toString());
        innerClass.addJavaDocLine(" */");
    }
    /**
     * 生成数据库备注字段作为注解
     * @param field
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addFieldComment(Field field,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        //如果需要生成数据库备注字段注解
        if (columnRemarks && StringUtility.stringHasValue(introspectedColumn.getRemarks())) {

            CommentUtil.AddJavaDocToField(field,introspectedColumn);
        }

        //如果支持注解就添加注解
        if (isAnnotations && isModel) {
            boolean isId = false;
            //添加主键注解
            for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
                if (introspectedColumn == column) {
                    isId = true;
                    field.addAnnotation("@Id");
                    field.addAnnotation("@GeneratedValue");
                    break;
                }
            }
            //不等于空
            if (!introspectedColumn.isNullable() && !isId){
                field.addAnnotation("@NotEmpty");
            }
            //添加主键生成策略
            if (introspectedColumn.isIdentity()) {
                if (introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement().equals("JDBC")) {
                    field.addAnnotation("@GeneratedValue(generator = \"JDBC\")");
                } else {
                    field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)");
                }
            } else if (introspectedColumn.isSequenceColumn()) {
                field.addAnnotation("@SequenceGenerator(name=\"\",sequenceName=\"" + introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement() + "\")");
            }
        }
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {


    }

    @Override
    public void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {

        StringBuilder sb = new StringBuilder();
        innerClass.addJavaDocLine("/**");
        innerClass
                .addJavaDocLine(" * bbbbbbbbbbbbbbbbbbbbbbbbbbb");
        sb.append(" * This class corresponds to the database table ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(sb.toString());
        innerClass.addJavaDocLine(" */");
    }

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {

        System.out.println(method.getName());
    }

}
