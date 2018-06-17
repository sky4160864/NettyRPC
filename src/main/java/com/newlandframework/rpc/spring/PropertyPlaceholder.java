package com.newlandframework.rpc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Huang Jianhai on 2018/6/15.
 * https://www.cnblogs.com/Gyoung/p/5507063.html
 */
public class PropertyPlaceholder extends PropertyPlaceholderConfigurer {


    private static Map<String,String> propertyMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        propertyMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            propertyMap.put(keyStr, value);
        }
    }

    //static method for accessing context properties
    public static String getProperty(String name) {
        return getProperty(name,null);
    }
    public static String getProperty(String name,String defualtVal) {
        if(propertyMap.containsKey(name)){
            return propertyMap.get(name);
        }else{
            return defualtVal;
        }
    }

    /*public void setLocation(Resource location) {
        Resource resource = new Resource(System.getProperty("user.dir"));
        super.setLocation(location);
    }*/
}
