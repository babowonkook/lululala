package com.wakuang.hehe.pingtai;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wakuang.hehe.common.ConstantParam;

@Controller
public class BigyoController {
    @Autowired
    private SearchPingtaiProxyFactory searchPingtaiProxyFactory;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String SilXiGanBigyo(Locale locale,
                                Model model) {
        SearchPingtaiPrice searchPingtaiPrice = null;
        if (ConstantParam.PLAFORM_BITHUM.equals("BITTHUM")) {
            searchPingtaiPrice = searchPingtaiProxyFactory.getSearchPingtaiService(ConstantParam.PLAFORM_BITHUM);
        }
        return null;

    }

}
