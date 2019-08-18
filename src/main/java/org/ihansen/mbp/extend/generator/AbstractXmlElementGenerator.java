package org.ihansen.mbp.extend.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
* 生成Xml接口
* @author Winston.Wang
* @date 2019/8/6
* @version 1.0
**/
public interface AbstractXmlElementGenerator {
    /**
     *
     * @param parentElement
     * @param introspectedTable
     */
    void addElements(XmlElement parentElement, IntrospectedTable introspectedTable);
}
