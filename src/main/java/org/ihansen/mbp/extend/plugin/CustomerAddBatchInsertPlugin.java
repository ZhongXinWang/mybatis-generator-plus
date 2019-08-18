package org.ihansen.mbp.extend.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;

import java.util.Set;
import java.util.TreeSet;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
* 增加批量插入的方法
* @author Winston.Wang
* @date 2019/8/5
* @version 1.0
**/
public class CustomerAddBatchInsertPlugin extends  CustomerPluginAdapter {

    /**
     * 修改Mapper类增加批量插入方法
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        //增加批量插入
        addBatchInsertMethod(interfaze,introspectedTable);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    /**
     * 修改mapper.xml 支持批量插入
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        getSqlSupport().addBatchInsertXml(document, introspectedTable);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * 方法定义
     * @param interfaze
     * @param introspectedTable
     */
    private void addBatchInsertMethod(Interface interfaze, IntrospectedTable introspectedTable) {
        // 设置需要导入的类
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        importedTypes.add(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));

        Method ibsmethod = new Method();
        // 1.设置方法可见性
        ibsmethod.setVisibility(JavaVisibility.PUBLIC);
        // 2.设置返回值类型
        FullyQualifiedJavaType ibsreturnType = FullyQualifiedJavaType.getIntInstance();
        ibsmethod.setReturnType(ibsreturnType);
        // 3.设置方法名
        ibsmethod.setName("insertBatch");
        // 4.设置参数列表
        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType paramListType;
        if (introspectedTable.getRules().generateBaseRecordClass()) {
            paramListType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }
        else if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            paramListType = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
        } else {
            throw new RuntimeException(getString("RuntimeError.12"));
        }
        paramType.addTypeArgument(paramListType);

        ibsmethod.addParameter(new Parameter(paramType, "records"));

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(ibsmethod);
    }

}
