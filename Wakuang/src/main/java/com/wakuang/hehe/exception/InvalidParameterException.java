package com.wakuang.hehe.exception;


/**
 * POST Request Parameter 검증 실패
 * 
 * <p>1. Request Parameter 가 nul 인 경우
 * <p>2. 각 API 별로 필수값 및 값의 Type 을 확인하여 정의된 schema에 맞지 않는 경우 발생
 *
 */
public class InvalidParameterException extends Exception {

    private static final long serialVersionUID = -6019791722215596636L;

    public InvalidParameterException(){
        super("PARAMETER_INVALID");
    }
    
    public InvalidParameterException(String message) {
        super(message);
    }
}
