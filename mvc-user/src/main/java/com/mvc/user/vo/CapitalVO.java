package com.mvc.user.vo;

import com.mvc.user.entity.Capital;
import com.mvc.user.util.CoinUtil;
import lombok.Data;

/**
 * Capital vo
 *
 * @author qiyichen
 * @create 2018/1/9 17:29
 */
@Data
public class CapitalVO extends Capital {

    public Double getBalanceStr(){
        return  CoinUtil.wei2Value(super.getCoinId(), super.getBalance());
    }

    public Double getLockStr(){
        return  CoinUtil.wei2Value(super.getCoinId(), super.getLocked());
    }

    public Double getInterestStr(){
        return  CoinUtil.wei2Value(super.getCoinId(), super.getInterest());
    }

    public String getUnit(){
        return CoinUtil.getUnit(super.getCoinId());
    }

    public String getName(){
        return CoinUtil.getName(super.getCoinId());
    }

    public String getNameEn(){
        return CoinUtil.getNameEn(super.getCoinId());
    }
}
