package com.mvc.console.rest;

import com.github.pagehelper.PageInfo;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import com.mvc.common.rest.BaseController;
import com.mvc.console.dto.LockRecordDTO;
import com.mvc.console.entity.LockRecord;
import com.mvc.console.service.LockRecordService;
import com.mvc.console.vo.LockRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("lockRecord")
public class LockRecordController extends BaseController<LockRecordService, LockRecord> {

    @Autowired
    private LockRecordService lockRecordService;

    @RequestMapping(value = "lock", method = RequestMethod.POST)
    public Result lock(@RequestBody LockRecordDTO lockRecordDTO) throws UnsupportedEncodingException {
        lockRecordService.lock(lockRecordDTO);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public PageInfo<LockRecordVO> list(LockRecordDTO lockRecordDTO) {
        return lockRecordService.list(lockRecordDTO);
    }
}
