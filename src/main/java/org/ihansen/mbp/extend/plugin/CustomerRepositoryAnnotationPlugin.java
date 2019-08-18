package org.ihansen.mbp.extend.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
* 给Mapper接口添加@Repository注解
* @author Winston.Wang
* @date 2019/8/8
* @version 1.0
**/
public class CustomerRepositoryAnnotationPlugin extends PluginAdapter {

    private FullyQualifiedJavaType annotationRepository;
    private String annotation = "@Repository";

    public CustomerRepositoryAnnotationPlugin() {
        //添加包路径
        annotationRepository = new FullyQualifiedJavaType("org.springframework.stereotype.Repository");
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    /**
     * 修改生成的Mapper接口
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(annotationRepository);
        interfaze.addAnnotation(annotation);
        return true;
    }
}
