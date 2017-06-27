package com.wakuang.hehe.hanguo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.hanguo.service.HanguoService;
import com.wakuang.hehe.pingtai.SearchPingtaiPrice;
import com.wakuang.hehe.pingtai.SearchPingtaiProxyFactory;

@Controller
@RequestMapping("/wakuang/hanguo")
public class HanguoController {
    
    @Autowired
    private HanguoService hanguoService;
    
    @Autowired
    private SearchPingtaiProxyFactory factory;
    
    private SearchPingtaiPrice pingTaiService;
    
    @ResponseBody
    @RequestMapping("/getTicker")
    public String getTicker(@RequestParam(defaultValue = "", value = ConstantParam.TYPE, required = false) String type) throws Exception {
        return hanguoService.getTicker(type);
    }
    
    @ResponseBody
    @RequestMapping("/getOrderbook")
    public String getOrderbook(@RequestParam(defaultValue = "", value = ConstantParam.TYPE, required = false) String type) throws Exception {
        return hanguoService.getOrderbook(type);
    }
    
    @ResponseBody
    @RequestMapping("/getTransaction")
    public String getTransaction(@RequestParam(defaultValue = "", value = ConstantParam.TYPE, required = false) String type) throws Exception {
        return hanguoService.getTransaction(type);
    }
}
