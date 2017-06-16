package com.wakuang.hehe.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wakuang.hehe.exception.InvalidParameterException;

public class CBTStringUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CBTStringUtils.class);

    private CBTStringUtils() {
    }

    /**
     * Object를 String 형태로 변환한다. default 로 Object의 value값이 null 인 경우 해당 key&value 값은 String으로 변환하지 않는다.
     * @param value
     * @return
     * @throws JsonProcessingException
     */
    public static String beanToString(Object value) throws JsonProcessingException {
        return beanToString(value, false);
    }

    /**
     * Object를 String 형태로 변환한다.
     * @param value
     * @param isNull false: Object의 value값이 null 인 경우 해당 key&value 값은 String으로 변환하지 않는다.
     * @return
     * @throws JsonProcessingException
     */
    public static String beanToString(Object value,
                                      boolean isNull) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (isNull) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        return mapper.writeValueAsString(value);
    }

    /**
     * String value 를 JsonNode로 변환한다.
     * @param value
     * @return
     * @throws IOException
     */
    public static JsonNode stringToJsonNode(String value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(value);
    }

    /**
     * JsonNode 를 Class type 에 맞게 변환한다.
     * @param jsonNode
     * @param classz
     * @return
     */
    public static Object convertValue(JsonNode jsonNode,
                                      Class<?> classz) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(jsonNode, classz);
    }

    /**
     * JsonNode 를 Map<String, Object> 형태로 변환한다.
     * @param jsonNode
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonNodeToMap(JsonNode jsonNode) {
        return (Map<String, Object>) convertValue(jsonNode, Map.class);
    }

    /**
     * JsonNode 를 Map<String, Object> 형태로 변환한다.
     * @param jsonNode
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object>[] jsonNodeToArray(JsonNode jsonNode) {
        return (Map<String, Object>[]) convertValue(jsonNode, Map[].class);
    }

    public static String defaultString(Object obj) {
        return defaultString(obj, "");
    }

    public static String defaultString(Object obj,
                                       String defaultStr) {
        if (obj != null && !"null".equals(obj)) {
            return obj + "";
        }
        return defaultStr;
    }

    /**
     * String value 값을 BigDecimal형으로 생성한다.
     * @param value
     * @param defaultValue
     * @return
     */
    public static BigDecimal stringToBigDecimal(String value,
                                                String defaultValue) {
        BigDecimal result = null;
        try {
            result = NumberUtils.createBigDecimal(StringUtils.defaultIfEmpty(value, defaultValue).trim());
        } catch (NumberFormatException e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("value: {}", value, e);
            }
            result = new BigDecimal(defaultValue);
        }
        return result;
    }

    /**
     * String 값의 blank 여부를 확인한다.
     * @param arguments
     * @return
     */
    public static boolean isBlankString(String... arguments) {
        for (String argument : arguments) {
            if (StringUtils.isBlank(argument)) {
                return true;
            }
        }
        return false;
    }

    /**
     * String empty 여부를 확인한다.
     * @param arguments
     * @throws InvalidParameterException
     */
    public static void checkBlankString(String... arguments) throws InvalidParameterException {
        for (String argument : arguments) {
            if (StringUtils.isEmpty(argument)) {
                throw new InvalidParameterException("");
            }
        }
    }

    /**
     * BigDecimal 값의 null 여부를 확인한다.
     * @param arguments
     * @throws InvalidParameterException
     */
    public static void checkNullBigDecimal(BigDecimal... arguments) throws InvalidParameterException {
        for (BigDecimal argument : arguments) {
            if (argument == null) {
                throw new InvalidParameterException("");
            }
        }
    }

    /**
     * String Array 의 값을 확인한다.
     * @param arguments
     * @throws InvalidParameterException
     */
    public static void isEmptyArray(String[] arguments) throws InvalidParameterException {
        checkBlankString(arguments);
    }

    /**
     * BigDecimal 의 value 값이 null, 0 인지 여부를 확인한다.
     * @param argument
     * @return true: null or 0 일 때
     */
    public static boolean isNullBigDecimal(BigDecimal argument) {
        return isNullBigDecimal(argument, BigDecimal.ZERO);
    }

    /**
     * BigDecimal 의 value 값이 null, 비교값과 동일한지 여부를 확인한다.
     * @param argument
     * @param compareValue
     * @return true: null or 0 일 때
     */
    public static boolean isNullBigDecimal(BigDecimal argument,
                                           BigDecimal compareValue) {
        boolean result = false;
        if (argument == null || compareValue.compareTo(argument) == 0) {
            result = true;
        }
        return result;
    }

    /**
     * BigDecimal 의 value 값이 null 인 경우, defaultValue 0을 반환한다.
     * @param argument
     * @return
     */
    public static BigDecimal defaultBigDecimal(BigDecimal argument) {
        return defaultBigDecimal(argument, BigDecimal.ZERO);
    }

    /**
     * BigDecimal 의 value 값이 null 인 경우, defaultValue 를 반환한다.
     * @param argument
     * @param defaultValue
     * @return
     */
    public static BigDecimal defaultBigDecimal(BigDecimal argument,
                                               BigDecimal defaultValue) {
        if (isNullBigDecimal(argument)) {
            return defaultValue;
        }
        return argument;
    }

    /**
     * Object 의 value 값이 null 인 경우, defaultValue 를 반환한다.
     * @param argument
     * @param defaultValue
     * @return
     */
    public static BigDecimal defaultBigDecimal(Object argument,
                                               BigDecimal defaultValue) {
        if (argument == null) {
            return defaultValue;
        }
        return defaultBigDecimal((BigDecimal) argument, defaultValue);
    }

    /**
     * Object 의 value 값이 null 인 경우, defaultValue 를 반환한다.
     * @param argument
     * @param defaultValue
     * @return
     */
    public static BigDecimal defaultBigDecimal(Object argument,
                                               Object defaultValue) {
        return defaultBigDecimal(argument, (BigDecimal) defaultValue);
    }

    /**
     * @MethodName : xmlToJsonNode
     * @Description : xml value 를 JsonNode로 변환한다.
     * @Date : 2017. 5. 18.
     * @Author : 황원국
     * @param content
     * @return
     * @throws IOException
     */
    public static JsonNode xmlToJsonNode(String content) throws IOException {
        JSONObject jsonObj = XML.toJSONObject(content);
        return stringToJsonNode(jsonObj.toString());
    }

    public static String listToString(List<?> list) {
        return list.toString().replace("[", "").replace("]", "");
    }

    /**
     * long 값을 확인한다.
     * @param values
     * @throws InvalidParameterException
     */
    public static void checkLongValue(long... values) throws InvalidParameterException {
        for (long value : values) {
            if (value <= 0) {
                throw new InvalidParameterException("");
            }
        }
    }

    /**
     * 소문자를 대문자로변경
     * @param str
     * @return
     */
    public static String exChange(String str) {
        StringBuilder sb = new StringBuilder("");
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isLowerCase(c)) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 숫자여부
     * @param str
     * @return
     */
    public static boolean isNum(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public static String camel4underline(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    public static String strToCamelCase(String val) {
        if (StringUtils.isEmpty(val)) {
            return "";
        }
        String str = val.toLowerCase();
        StringBuilder sb = new StringBuilder();
        boolean low = true;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '_') {
                low = false;
            } else {
                if (low) {
                    sb.append(str.charAt(i));
                } else {
                    sb.append(Character.toUpperCase(str.charAt(i)));
                    low = true;
                }
            }
        }
        return sb.toString();
    }

    public static String objectToString(Object object) {
        if (object == null) {
            return "";
        }
        return object.toString();
    }

    /**
     * 숫자를 알파벳으로 변환한다.
     * @MethodName : toAlphabetic
     * @Description : int to Alphabet
     * @Date : 2017. 5. 11.
     * @Author : 이현구
     * @param i
     * @return String
     */
    public static String toAlphabetic(int i) {
        int quot = i / 26;
        int rem = i % 26;
        char letter = (char) ((int) 'A' + rem);
        if (quot == 0) {
            return "" + letter;
        } else {
            return toAlphabetic(quot - 1) + letter;
        }
    }

    /**
     * msg_key의 중복을 없애기 위해 새로운 키를 만든다.
     * @MethodName : getAlphabetKey
     * @Description : msg_key의 중복을 없애기 위해 새로운 키를 만든다.
     * @Date : 2017. 5. 11.
     * @Author : 이현구
     * @param key
     * @param msgCount
     * @return String
     */
    public static String getAlphabetKey(String key,
                                        int msgCount) {
        StringBuilder keyBuffer = new StringBuilder();
        keyBuffer.append(key);
        keyBuffer.append(".");
        keyBuffer.append(toAlphabetic(msgCount--));
        return keyBuffer.toString();
    }

    /**
     * 특수문자 제거
     * @MethodName : specialCharacterRemove
     * @Description : 특수문자 제거
     * @Date : 2017. 5. 11.
     * @Author : 이현구
     * @param str
     * @return String
     */
    public static String specialCharacterRemove(String str) {
        return str.replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "");
    }

    /**
     * 중복된 스페이스 제거
     * @MethodName : continueSpaceRemove
     * @Description : 중복된 스페이스 제거
     * @Date : 2017. 5. 11.
     * @Author : 이현구
     * @param str
     * @return
     */
    public static String continueSpaceRemove(String str) {
        return str.replaceAll("\\s{2,}", "");
    }

    /**
     * 숫자 제거
     * @MethodName : numericRemove
     * @Description : 숫자 제거
     * @Date : 2017. 5. 11.
     * @Author : 이현구
     * @param str
     * @return String
     */
    public static String numericRemove(String str) {
        return str.replaceAll("[0-9]", "");
    }

    /**
     * 다국어 메시지 키 생성
     * @MethodName : getMessageKey
     * @Description : 다국어 메시지 키 생성
     * @Date : 2017. 5. 11.
     * @Author : 이현구
     * @param enStr
     * @return String
     */
    public static String getMessageKey(String enStr) {
        String result = enStr.toLowerCase();
        result = specialCharacterRemove(result);
        result = continueSpaceRemove(result);
        result = numericRemove(result);
        result = result.replaceAll(" ", "_");
        result = result.replaceAll("__", "_");

        StringBuilder msgKey = new StringBuilder();
        if (result.contains("_")) {
            msgKey.append("sentence.");
        } else {
            msgKey.append("word.");
        }
        if (result.length() > 40) {
            result = result.substring(0, 40);
        }
        msgKey.append(result);
        return msgKey.toString();
    }

    /**
     * byte 단위로 substring
     * @param str
     * @param beginIndex 0부터 시작
     * @param endIndex substr할 바이트 수. 2-byte문자의 경우 바이트가 부족하면 그 앞 글자까지만 자름.
     * @param bytesForDB 2-byte문자(한글 등)의 DB에서의 바이트 수. 예를들어 오라클/UTF-8이면 3바이트임
     * @return
     */
    public static String subStrByte(String str,
                                    int beginIndex,
                                    int endIndex,
                                    int bytesForDB) {
        if (str == null) {
            return "";
        }

        String tmp = str;
        int slen = 0, blen = 0;
        char c;

        if (tmp.getBytes().length > endIndex - 1) { // 0부터 카운트 되므로 endIndex - 1
            while (blen + 1 < endIndex - 1) {
                c = tmp.charAt(slen);
                blen++;
                slen++;

                if (c > 127) {
                    blen = blen + (bytesForDB - 1); // 2-byte character
                }
            }

            tmp = tmp.substring(beginIndex, slen);
        }

        return tmp;
    }
}
