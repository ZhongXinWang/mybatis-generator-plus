package org.ihansen.mbp.extend.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
* Model类添加builder方式插件
* @author Winston.Wang
* @date 2019/8/5
* @version 1.0
**/
public class CustomerModelAddBuilderPlugin  extends  CustomerPluginAdapter{

    /**
    * 重写生成后的Model类，增加内部类builder
    * @author Winston.Wang
    * @date 2019/8/5
    * @param
    * @param
    * @return
    **/
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //将生成的Model Class传入进行修改
        addBuilder(topLevelClass);
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    /**
    * 添加Builder构造方式
    * @author Winston.Wang
    * @date 2019/8/5
    * @param
    * @param
    * @return
    **/
    private void addBuilder(TopLevelClass topLevelClass) {
        //构造内部静态类
        InnerClass builder = new InnerClass("Builder");
        builder.setStatic(true);
        //设置为public
        builder.setVisibility(JavaVisibility.PUBLIC);
        //获取类的所有属性字段
        List<Field> fields = topLevelClass.getFields();
        for (Field field : fields) {
            //Builder不需要序列化
            if("serialVersionUID".equals(field.getName())){
                continue;
            }
            //Builder 不需要注解,重新设置对象
            Field f = setField(field);

            //Builder增加setter方法
            builder.addField(f);
            Method setter = new Method(f.getName());
            setter.setVisibility(JavaVisibility.PUBLIC);
            setter.setReturnType(new FullyQualifiedJavaType("Builder"));
            setter.addParameter(new Parameter(f.getType(), f.getName()));
            setter.addBodyLine("this."+f.getName()+" = "+f.getName()+";");
            setter.addBodyLine("return this;");
            builder.addMethod(setter);
        }

        /**
         * Builder 增加方法 builder
         * public void builder(){new Class(this)}
         */
        Method build = new Method("build");
        build.setVisibility(JavaVisibility.PUBLIC);
        build.setReturnType(new FullyQualifiedJavaType(topLevelClass.getType().getShortName()));
        build.addBodyLine("return new "+topLevelClass.getType().getShortName()+"(this);");
        builder.addMethod(build);
        topLevelClass.addInnerClass(builder);

        //Model类添加私有构造方法，用来初始化参数
        Method constructor = new Method(topLevelClass.getType().getShortName());
        constructor.setConstructor(true);
        constructor.setVisibility(JavaVisibility.PRIVATE);
        //传入的参数  Builder
        constructor.addParameter(new Parameter(new FullyQualifiedJavaType("Builder"), "b"));
        for (Field field : fields) {
            // 将序列化的serialVersionUIDg过滤掉
            if("serialVersionUID".equals(field.getName())){
                continue;
            }
            constructor.addBodyLine(field.getName()+" = b."+field.getName()+";");
        }
        topLevelClass.addMethod(constructor);

        //添加一个无参的构造方法
        Method defConstructor = new Method(topLevelClass.getType().getShortName());
        defConstructor.setConstructor(true);
        defConstructor.setVisibility(JavaVisibility.PUBLIC);
        defConstructor.addBodyLine("super();");
        topLevelClass.addMethod(defConstructor);
    }

    /**
     * 设置Field字段
     * @param field
     * @return
     */
    private Field setField(Field field) {

        Field f = new Field();
        f.setName(field.getName());
        f.setType(field.getType());
        f.setVisibility(field.getVisibility());
        //设置注释
        field.getJavaDocLines().stream().forEach(a->{

            f.addJavaDocLine(a);

        });

        return f;
    }

}
