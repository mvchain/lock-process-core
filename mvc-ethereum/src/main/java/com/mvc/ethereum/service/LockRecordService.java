package com.mvc.ethereum.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.common.biz.BaseBiz;
import com.mvc.common.dto.LockRecordDTO;
import com.mvc.ethereum.mapper.LockRecordMapper;
import com.mvc.ethereum.model.LockRecord;
import com.mvc.ethereum.model.vo.LockRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private RedisTemplate redisTemplate;

    public PageInfo<LockRecordVO> list(LockRecordDTO lockRecordDTO) {
        PageHelper.startPage(lockRecordDTO.getPageNo(), lockRecordDTO.getPageSize());
        List<LockRecordVO> result = lockRecordMapper.list(lockRecordDTO);
        return new PageInfo<>(result);
    }

    public List<LockRecord> selectUnlock() {
        return lockRecordMapper.selectUnlock();
    }

    public void update(LockRecord lockRecord) {
        lockRecordMapper.updateByPrimaryKey(lockRecord);
    }

    public void addUnLockRecord(Integer times) {
        lockRecordMapper.addUnLockRecord(times);
    }

    public void updateUnlock(Integer times) {
        lockRecordMapper.updateUnlock();
        lockRecordMapper.updateUnlockBalance(times);
    }
}
