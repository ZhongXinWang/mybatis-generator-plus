package org.ihansen.mbp.extend.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;

/**
* 给java类添加方法接口
* @author Winston.Wang
* @date 2019/8/6
* @version 1.0
**/
public interface AbstractJavaMapperMethodGenerator {
    /**
     *
     * @param interfaze
     * @param introspectedTable
     */
    void addMethod(Interface interfaze, IntrospectedTable introspectedTable);
}
