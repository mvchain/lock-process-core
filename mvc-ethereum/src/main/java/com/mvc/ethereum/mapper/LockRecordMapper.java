package com.mvc.ethereum.mapper;

import com.mvc.common.dto.LockRecordDTO;
import com.mvc.ethereum.model.LockRecord;
import com.mvc.ethereum.model.vo.LockRecordVO;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qyc
 */
public interface LockRecordMapper extends Mapper<LockRecord> {
    /**
     * list
     *
     * @param lockRecordDTO
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM lock_record",
            "WHERE 1 = 1",
            "<when test=\"key!=null and key !=''\">",
            "AND (user_id = #{key} or order_id = #{key})",
            "</when>",
            "order by id desc",
            "</script>"})
    List<LockRecordVO> list(LockRecordDTO lockRecordDTO);

    /**
     * selectUnlock
     *
     * @return
     */
    @Select("SELECT * FROM lock_record WHERE updated_at <= now() and status = 0")
    List<LockRecord> selectUnlock();

    @Select("INSERT INTO unlock_record(user_id, quantity, coin_id, month) SELECT user_id, floor(sum(quantity + interest)/6) balance, coin_id, #{times} FROM lock_record WHERE coin_id = 1 GROUP BY coin_id, user_id")
    void addUnLockRecord(Integer times);

    @Update("UPDATE capital t1, unlock_record t2 SET t1.balance = t1.balance + t2.quantity, t1.locked = t1.locked - t2.quantity WHERE t1.user_id = t2.user_id AND t1.coin_id = t2.coin_id AND t2.`month` = #{times}")
    void updateUnlockBalance(Integer times);

    @Update("update lock_record set status = 1")
    void updateUnlock();
}