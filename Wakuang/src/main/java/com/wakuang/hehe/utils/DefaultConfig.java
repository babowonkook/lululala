package com.wakuang.hehe.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class DefaultConfig {

    private static DefaultConfig       defaultConfig    = null;
    private static final Logger        LOG              = LoggerFactory.getLogger(DefaultConfig.class);
    private static final String        CONFIG_PATH      = "classpath:config.xml";
    private static Map<String, Object> defaultConfigMap = new HashMap<String, Object>();

    private DefaultConfig() {
    }

    public static DefaultConfig getInstance() throws ParserConfigurationException, SAXException, IOException {
        if (defaultConfig == null) {
            defaultConfig = new DefaultConfig();
            defaultConfig.init();
        }
        return defaultConfig;
    }

    private void init() throws ParserConfigurationException, SAXException, IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader(getClass().getClassLoader());

        String configPath = StringUtils.defaultString(System.getProperty("cbt_conf_name"), CONFIG_PATH);

        Resource viewResource = resourceLoader.getResource(configPath);

        DefaultHandler handler = new CBTDefaultHandler();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(viewResource.getInputStream(), handler);
        LOG.debug("configPath: {} ", configPath);
        LOG.debug("DefaultConfig: {} ", defaultConfigMap.toString());
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSelection(String section) {
        return (Map<String, Object>) DefaultConfig.defaultConfigMap.get(section);
    }

    public static String getValueString(String section,
                                        String key) {
        Map<String, Object> sectionMap = getSelection(section);
        return (String) sectionMap.get(key);
    }

    public static Object getValue(String section,
                                  String key) {
        Map<String, Object> sectionMap = getSelection(section);
        return sectionMap.get(key);
    }

    private class CBTDefaultHandler extends DefaultHandler {
        Map<String, Object> entryMap = null;
        String              section  = null;

        @SuppressWarnings("unchecked")
        @Override
        public void startElement(String uri,
                                 String localName,
                                 String qName,
                                 Attributes attributes) throws SAXException {
            if ("section".equalsIgnoreCase(qName)) {
                section = attributes.getValue("name");
            } else if ("entry".equalsIgnoreCase(qName)) {
                entryMap = (HashMap<String, Object>) defaultConfigMap.get(section);
                if (entryMap == null) {
                    entryMap = new HashMap<String, Object>();
                }
                String key = attributes.getValue("key");
                String value = attributes.getValue("value");
                entryMap.put(key, value);
                defaultConfigMap.put(section, entryMap);
            }
        }
    }

    /**
     * <pre>
     * CBT 공용계정사용시 처리. 이벤트 테스트를 위해서 이벤트 시작 날짜를 하루 먼저 시작함. 해당 계정만 허용 cbt-test@hanmail.ne , g_team@sina.com 계정의 회원번호는 config.xml 에서 설정.
     * @param memNo
     * @return
     */
    public static boolean isTestMemNo(BigDecimal memNo) {
        String testID = DefaultConfig.getValueString("CONFIG_FREE_CODE_KEY", "CONFIG_TEST_ID");
        boolean result = memNo != null && Pattern.matches(testID, memNo.toString());
        return result;
    }
}
