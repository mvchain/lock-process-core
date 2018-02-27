package com.mvc.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.common.biz.BaseBiz;
import com.mvc.console.entity.Capital;
import com.mvc.console.mapper.CapitalMapper;
import com.mvc.console.vo.CapitalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CapitalService extends BaseBiz<CapitalMapper,Capital> {

    @Autowired
    private CapitalMapper capitalMapper;

    public List<CapitalVO> selectByUserId(Capital capital ) {
        PageHelper.startPage(0, 10);
        List<CapitalVO> page = capitalMapper.selectByUserId(capital.getUserId());
        return  page;
    }

    public void updateValue(BigInteger quantity, BigInteger userId, BigInteger coinId, BigInteger interest) {
        capitalMapper.updateValue(quantity, userId, coinId, interest);
    }

    public void updateBalance(BigInteger coinId, BigInteger userId, BigInteger value) {
        capitalMapper.updateBalance(coinId, userId, value);
    }
}
