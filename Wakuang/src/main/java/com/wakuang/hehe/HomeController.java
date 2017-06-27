package com.wakuang.hehe;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.common.WakuangResult;
import com.wakuang.hehe.pingtai.PingTaiTradeService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/home")
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private PingTaiTradeService service;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "index";
	}
	
	@RequestMapping(value = "/buyao", method = RequestMethod.GET)
	public String buyao(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "index2";
	}
	
    @RequestMapping(value = "/buyaos", method = RequestMethod.GET)
    public String buyaos(Locale locale,
                         Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        return "buyaos";
    }

	@RequestMapping(value = "/yieldRate", method = RequestMethod.GET)
	@ResponseBody
	public Object yieldRate(@RequestParam(defaultValue = "", value = ConstantParam.BUY_PRICE, required = false) String buyPrice,
							@RequestParam(defaultValue = "", value = ConstantParam.BUY_PLATFORM, required = false) String buyPlatform,
							@RequestParam(defaultValue = "", value = ConstantParam.COINTYPE, required = false) String coinType,
							@RequestParam(defaultValue = "", value = ConstantParam.BUY_NUM, required = false) String buyNum,
							@RequestParam(defaultValue = "", value = ConstantParam.SELL_PLATFORM, required = false) String sellPlatform,
							@RequestParam(defaultValue = "", value = ConstantParam.SELL_PRICE, required = false) String sellPrice,
							@RequestParam(defaultValue = "", value = ConstantParam.RATE, required = false) String rate,
							@RequestParam(defaultValue = "", value = "tufaQingkuang", required = false) String tk) {
		WakuangResult result = new WakuangResult();
		BigDecimal totalPrice = new BigDecimal(buyPrice).multiply(new BigDecimal(buyNum));
		BigDecimal coinNum = service.buyCoin(buyPlatform, coinType, totalPrice, new BigDecimal(buyPrice), tk);
		BigDecimal zuiZhongPrice = service.sellCoin(sellPlatform, coinType, coinNum, new BigDecimal(sellPrice), tk);
		BigDecimal shouxufei = new BigDecimal("0");
		if(!ConstantParam.PLAFORM_BITHUM.equals(buyPlatform)) {
			totalPrice = totalPrice.multiply(new BigDecimal(rate));
		}
		if(!ConstantParam.PLAFORM_BITHUM.equals(sellPlatform)) {
			zuiZhongPrice = zuiZhongPrice.multiply(new BigDecimal(rate));
			shouxufei = new BigDecimal("0.0005");
		}
		BigDecimal lirun = zuiZhongPrice.subtract(totalPrice);
		BigDecimal lirunLv = lirun.divide(totalPrice,8,BigDecimal.ROUND_HALF_UP);
		Map<String, Object> rs = new HashMap<>();
		rs.put(ConstantParam.SHOUYI_E, lirun);
		rs.put(ConstantParam.SHOUYI_RATE, lirunLv);
		rs.put(ConstantParam.SHOUYI_E_YUAN, lirun.divide(new BigDecimal(rate),8,BigDecimal.ROUND_HALF_UP));
		rs.put(ConstantParam.TIKUAN_SHOUXU_FEI, zuiZhongPrice.multiply(shouxufei));
		rs.put(ConstantParam.TIKUAN_SHOUXU_FEI_YUAN, zuiZhongPrice.multiply(shouxufei).divide(new BigDecimal(rate),8,BigDecimal.ROUND_HALF_UP));
		result.setResult(rs);
		result.setResultCode("000");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/getData")
	public Object getData(@RequestParam(defaultValue = "", value = ConstantParam.RATE, required = false) String rate,
						  @RequestParam(defaultValue = "", value = ConstantParam.BUY_PRICE, required = false) String buyPrice,
						  @RequestParam(defaultValue = "", value = "tufaQingkuang", required = false) String tk) throws Exception {
		Map<String, Object> rs = service.compare(ConstantParam.PLAFORM_BIDUOBAO, ConstantParam.PLAFORM_BITHUM, rate, buyPrice, tk);
		return rs;
	}
	
}
