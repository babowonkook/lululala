package com.wakuang.hehe.hanguo.service;

import java.util.List;
import java.util.Map;

public abstract interface CodeService {
	List<Map<String, String>> getCode(String code);
}
