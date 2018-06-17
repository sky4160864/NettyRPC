package com.newlandframework.test.spring.schema;

/**
 * Created by Administrator on 2018-06-17.
 */
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class UserNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("user", new UserBeanDefinitionParser2());
    }
}
