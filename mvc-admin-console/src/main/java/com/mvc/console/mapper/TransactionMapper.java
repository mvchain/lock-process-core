package com.mvc.console.mapper;

import com.mvc.console.entity.Transaction;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @author qyc
 */
public interface TransactionMapper extends Mapper<Transaction>, InsertListMapper<Transaction> {

}
