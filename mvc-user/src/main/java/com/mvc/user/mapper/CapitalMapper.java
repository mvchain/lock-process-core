package com.mvc.user.mapper;

import com.mvc.user.entity.Capital;
import com.mvc.user.vo.CapitalVO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qyc
 */
public interface CapitalMapper extends Mapper<Capital> {
    /**
     * list
     *
     * @param capital
     * @return
     */
    @Select("select * from capital where user_id = #{userId} order by coin_id asc")
    List<CapitalVO> list(Capital capital);
}