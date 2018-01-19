package com.mvc.console.service;

import com.mvc.common.biz.BaseBiz;
import com.mvc.console.entity.RefundApplication;
import com.mvc.console.mapper.RefundApplicationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RefundApplicationService extends BaseBiz<RefundApplicationMapper,RefundApplication> {

}
