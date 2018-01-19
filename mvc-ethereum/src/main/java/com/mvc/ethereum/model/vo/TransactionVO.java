package com.mvc.ethereum.model.vo;

import com.mvc.ethereum.model.Transaction;
import com.mvc.ethereum.utils.CoinUtil;
import lombok.Data;

/**
 * TransactionVO
 *
 * @author qiyichen
 * @create 2018/1/15 14:33
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
