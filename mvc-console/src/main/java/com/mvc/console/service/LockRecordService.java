package com.mvc.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.common.biz.BaseBiz;
import com.mvc.common.context.BaseContextHandler;
import com.mvc.console.dto.LockRecordDTO;
import com.mvc.console.entity.Capital;
import com.mvc.console.entity.LockRecord;
import com.mvc.console.mapper.LockRecordMapper;
import com.mvc.console.util.CoinUtil;
import com.mvc.console.util.ConfigUtil;
import com.mvc.console.vo.LockRecordVO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LockRecordService extends BaseBiz<LockRecordMapper, LockRecord> {

    @Autowired
    private LockRecordMapper lockRecordMapper;
    @Autowired
    private CapitalService capitalService;
    @Autowired
    private RedisTemplate redisTemplate;

    public void lock(LockRecordDTO lockRecordDTO) {
        // 金额校验
        checkLock(lockRecordDTO);

        Long seq = redisTemplate.opsForValue().increment("TRANSATION_S", 1);
        Float interestRate = Float.valueOf(ConfigUtil.lockConfigMap.get(lockRecordDTO.getType()).getInterest());
        // 插入锁仓记录
        lockRecordDTO.setOrderId("S" + String.format("%09d", seq));
        lockRecordDTO.setStatus(0);
        lockRecordDTO.setMonth(Integer.valueOf(ConfigUtil.lockConfigMap.get(lockRecordDTO.getType()).getMonth()));
        lockRecordDTO.setQuantity(CoinUtil.Value2wei(lockRecordDTO.getValue(), lockRecordDTO.getType()));
        lockRecordDTO.setInterestRate(interestRate);
        BigDecimal value = lockRecordDTO.getValue().multiply(new BigDecimal(interestRate));
        lockRecordDTO.setInterest(CoinUtil.Value2wei(value, lockRecordDTO.getType()));
        lockRecordDTO.setCoinId(CoinUtil.getId(lockRecordDTO.getType()));
        lockRecordDTO.setUserId(BaseContextHandler.getUserIDInt());
        lockRecordDTO.setUpdatedAt(DateTime.now().plusMonths(lockRecordDTO.getMonth()).toDate());
        insert(lockRecordDTO);
        // 修改余额
        capitalService.updateValue(lockRecordDTO.getQuantity(), lockRecordDTO.getUserId(), lockRecordDTO.getCoinId(), lockRecordDTO.getInterest());
    }

    private void checkLock(LockRecordDTO lockRecordDTO) {

        Capital capital = new Capital();
        capital.setUserId(BaseContextHandler.getUserIDInt());
        capital.setCoinId(CoinUtil.getId(lockRecordDTO.getType()));
        Capital capitalTemp = capitalService.selectOne(capital);
        Float interestRate = Float.valueOf(ConfigUtil.lockConfigMap.get(lockRecordDTO.getType()).getInterest().toString());
        BigInteger limit = CoinUtil.Value2wei(ConfigUtil.lockConfigMap.get(lockRecordDTO.getType()).getMax(), lockRecordDTO.getType());
        BigInteger locked = null == capitalTemp ? BigInteger.ZERO : capitalTemp.getLocked();
        BigInteger balance = null == capitalTemp ? BigInteger.ZERO : capitalTemp.getBalance();
        BigInteger lockValue = CoinUtil.Value2wei(lockRecordDTO.getValue(), lockRecordDTO.getType());
        if (ConfigUtil.lockConfigMap.get(lockRecordDTO.getType()).getSwitchKey() == 1) {
            // 锁仓上限 - 已锁仓金额 - 需要锁仓的金额 > 0 且 余额 - 需要锁仓的金额 > 0
            Assert.isTrue(limit.subtract(locked).subtract(lockValue).compareTo(BigInteger.ZERO) >= 0 && balance.subtract(locked).compareTo(BigInteger.ZERO) >= 0, "余额不足");
        }
        if (null == capital) {
            capitalTemp = new Capital();
            capitalTemp.setUserId(BaseContextHandler.getUserIDInt());
            capitalTemp.setCoinId(CoinUtil.getId(lockRecordDTO.getType()));
            capitalTemp.setBalance(BigInteger.ZERO);
            capitalTemp.setStatus(0);
            capitalTemp.setLocked(lockValue);
            capitalTemp.setInterest(CoinUtil.Value2wei(lockRecordDTO.getValue().multiply(new BigDecimal(interestRate)), lockRecordDTO.getType()));
            capitalTemp.setUpdatedAt(DateTime.now().plusMonths(Integer.valueOf(ConfigUtil.lockConfigMap.get(lockRecordDTO.getType()).getMonth())).toDate());
            capitalService.insert(capitalTemp);
        }

    }

    public PageInfo<LockRecordVO> list(LockRecordDTO lockRecordDTO) {
        PageHelper.startPage(lockRecordDTO.getPageNo(), lockRecordDTO.getPageSize());
        lockRecordDTO.setUserId(BaseContextHandler.getUserIDInt());
        List<LockRecordVO> result = lockRecordMapper.list(lockRecordDTO);
        return new PageInfo<LockRecordVO>(result);
    }
}
