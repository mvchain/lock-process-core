package com.mvc.console.vo;

import com.mvc.console.entity.LockRecord;
import com.mvc.console.util.CoinUtil;
import lombok.Data;

/**
 * LockRecordVO
 *
 * @author qiyichen
 * @create 2018/1/11 19:54
 */
@Data
public class LockRecordVO extends LockRecord {

    public String type(){
        return CoinUtil.getUnit(super.getCoinId());
    }

    public Double getQuantityStr(){
        return CoinUtil.wei2Value( super.getCoinId(), super.getQuantity());
    }

    public Double getInterestStr(){
        return CoinUtil.wei2Value( super.getCoinId(), super.getInterest());
    }
}
