package com.wakuang.hehe.hanguo.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wakuang.hehe.pingtai.BaseJunitTest;

public class CodeServiceSourceImplTest extends BaseJunitTest{
	
	@Autowired
	@Qualifier("codeServiceSourceImpl")
	private CodeService codeServiceSourceImpl;

	@Test
	public void test() {
		List<Map<String, String>> haha = new ArrayList();
		haha=codeServiceSourceImpl.getCode("CD002");
		System.out.println(haha.toString());
		assertNotNull(codeServiceSourceImpl);
	}

}
