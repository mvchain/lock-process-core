package com.mvc.ethereum.mapper;

import com.mvc.common.dto.LockRecordDTO;
import com.mvc.ethereum.model.LockRecord;
import com.mvc.ethereum.model.vo.LockRecordVO;
import org.apache.ibatis.annotations.Select;
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
}