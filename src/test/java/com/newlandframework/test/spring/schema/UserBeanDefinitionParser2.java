package com.newlandframework.test.spring.schema;

import com.newlandframework.rpc.spring.NettyRpcService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class UserBeanDefinitionParser2 implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String id = element.getAttribute("id");
        String name = element.getAttribute("name");
        String sex = element.getAttribute("sex");
        int age = Integer.parseInt(element.getAttribute("age"));

        RootBeanDefinition bean = new RootBeanDefinition();
        bean.setBeanClass(User.class);
        bean.setLazyInit(false);
        bean.getPropertyValues().addPropertyValue("id", id);
        bean.getPropertyValues().addPropertyValue("name", name);
        bean.getPropertyValues().addPropertyValue("sex", sex);
        bean.getPropertyValues().addPropertyValue("age", age);
        parserContext.getRegistry().registerBeanDefinition(id, bean);
        return bean;
    }
}
