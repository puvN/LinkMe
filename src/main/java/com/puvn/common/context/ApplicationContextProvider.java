package com.puvn.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {
    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        AppContext.setApplicationContext(applicationContext);
    }
}
