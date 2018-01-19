package com.mvc.console.mapper;

import com.mvc.console.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface TransactionMapper extends Mapper<Transaction>, InsertListMapper<Transaction> {

}
