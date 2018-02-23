package com.wakuang.hehe.hanguo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service(value = "codeServiceSourceImpl")
public class CodeServiceSourceImpl implements CodeService {

	@Override
	public List<Map<String, String>> getCode(String code) {
		// TODO Auto-generated method stub
		List<Map<String, String>> codeList = new ArrayList<>();
		switch (code) {
		case "CD002":
			Map<String, String> codeBTC = new HashMap<>();
			codeBTC.put("CODE", "CD002");
			codeBTC.put("CODE_DETAIL", "BTC");
			codeList.add(codeBTC);
			Map<String, String> codeETH = new HashMap<>();
			codeETH.put("CODE", "CD002");
			codeETH.put("CODE_DETAIL", "ETH");
			codeList.add(codeETH);
			Map<String, String> codeETC = new HashMap<>();
			codeETC.put("CODE", "CD002");
			codeETC.put("CODE_DETAIL", "ETC");
			codeList.add(codeETC);
			Map<String, String> codeXRP = new HashMap<>();
			codeXRP.put("CODE", "CD002");
			codeXRP.put("CODE_DETAIL", "XRP");
			codeList.add(codeXRP);
			Map<String, String> codeLTC = new HashMap<>();
			codeLTC.put("CODE", "CD002");
			codeLTC.put("CODE_DETAIL", "LTC");
			codeList.add(codeLTC);
			break;
			
		case "CD003":
			Map<String, String> codeTotal = new HashMap<>();
			codeTotal.put("CODE", "CD003");
			codeTotal.put("CODE_DETAIL", "total");
			codeList.add(codeTotal);
			Map<String, String> codeIn_use = new HashMap<>();
			codeIn_use.put("CODE", "CD003");
			codeIn_use.put("CODE_DETAIL", "In_use");
			codeList.add(codeIn_use);
			Map<String, String> codeAvailable = new HashMap<>();
			codeAvailable.put("CODE", "CD003");
			codeAvailable.put("CODE_DETAIL", "Available");
			codeList.add(codeAvailable);
			break;

		default:
			break;
		}
		return codeList;
	}

}
