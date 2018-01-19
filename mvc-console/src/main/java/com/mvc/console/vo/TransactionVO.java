package com.mvc.console.vo;

import com.mvc.console.entity.Transaction;
import com.mvc.console.util.CoinUtil;
import lombok.Data;

/**
 * Capital vo
 *
 * @author qiyichen
 * @create 2018/1/9 17:29
 */
@Data
public class TransactionVO extends Transaction {

    public Double getQuantityStr(){
        return  CoinUtil.wei2Value(super.getCoinId(), super.getQuantity());
    }

    public Double getActualQuantityStr(){
        return  CoinUtil.wei2Value(super.getCoinId(), super.getActualQuantity());
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
