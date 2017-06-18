package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.wakuang.hehe.utils.DefaultConfig;

@Service
public class ChargeService {

    private static Map<String, Object> delvAmtCode = null;

    public BigDecimal getFee(String feeType,
                             BigDecimal amount) {
        if (MapUtils.isEmpty(delvAmtCode)) {
            delvAmtCode = DefaultConfig.getSelection("");
        }
        return amount;

    }

}
