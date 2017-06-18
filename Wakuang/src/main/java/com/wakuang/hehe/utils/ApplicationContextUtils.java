package com.wakuang.hehe.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext CTX;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        CTX = context;
    }

    public static Object getBean(String beanName) {
        return CTX.getBean(beanName);
    }

}