package com.wakuang.hehe.pingtai;

import org.springframework.stereotype.Service;

import com.wakuang.hehe.utils.ApplicationContextUtils;

@Service
public class SearchPingtaiProxyFactory {

    public SearchPingtaiPrice getSearchPingtaiService(String beanName) {
        return (SearchPingtaiPrice) ApplicationContextUtils.getBean(beanName);
    }
}
