package com.mvc.console.util;

import com.mvc.console.entity.CoinInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * coin trans util
 *
 * @author qiyichen
 * @create 2018/1/9 17:38
 */
@Data
public class CoinUtil {

    public static HashMap<BigInteger, CoinInfo> coinMap = new HashMap<>();

    public static Double wei2Value(BigInteger coinId, BigInteger balance) {
        CoinInfo coinInfo = coinMap.get(coinId);
        BigDecimal b = new BigDecimal(balance);


        Double rslt = b.divide(new BigDecimal(coinInfo.getRatio()), coinInfo.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
        return rslt;
    }

    public static String getUnit(BigInteger coinId) {
        return coinMap.get(coinId).getAbbr();
    }

    public static String getName(BigInteger coinId) {
        return coinMap.get(coinId).getName();
    }

    public static String getNameEn(BigInteger coinId) {
        return coinMap.get(coinId).getNameEn();
    }
}
