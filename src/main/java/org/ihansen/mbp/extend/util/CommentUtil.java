package org.ihansen.mbp.extend.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.Date;
import java.util.Properties;

/**
* 生成注释工具类
* @author Winston.Wang
* @date 2019/8/11
* @version 1.0
**/
public class CommentUtil {

    /**
    * 给类添加注释
    * @author Winston.Wang
    * @date 2019/8/11
    * @param
    * @param
    * @return
    **/
    public static void AddJavaDocToClass(CompilationUnit compilationUnit, Properties properties){

        if(compilationUnit.isJavaInterface()){
            //给java接口添加注释
           Interface inteface = (Interface) compilationUnit;
           String MapperName = inteface.getType().getShortName();
           inteface.addJavaDocLine("/**");
           inteface.addJavaDocLine(" *  由Mybatis代码生成器生成"+MapperName+"接口类");
           inteface.addJavaDocLine(" *  @author "+properties.getProperty("author","Winston.Wang"));
           inteface.addJavaDocLine(" *  @date "+DateUtils.formatDateTime(new Date()));
           inteface.addJavaDocLine(" **/");
        }else{

            TopLevelClass topLevelClass = (TopLevelClass)compilationUnit;
            String ModelName = topLevelClass.getType().getShortName();
            topLevelClass.addJavaDocLine("/**");
            topLevelClass.addJavaDocLine(" *  由Mybatis代码生成器生成"+ModelName+"实体类");
            topLevelClass.addJavaDocLine(" *  @author "+properties.getProperty("author","Winston.Wang"));
            topLevelClass.addJavaDocLine(" *  @date "+DateUtils.formatDateTime(new Date()));
            topLevelClass.addJavaDocLine(" **/");
        }
    }

    /**
    * 给字段添加注释
    * @author Winston.Wang
    * @date 2019/8/11
    * @param
    * @param
    * @return
    **/
    public static void AddJavaDocToField(Field field, IntrospectedColumn introspectedColumn){

        field.addJavaDocLine("/**");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(introspectedColumn.getRemarks());
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */");
        //comments设置为null，就不会生成//注释
        field.setComments(null);
    }
}
