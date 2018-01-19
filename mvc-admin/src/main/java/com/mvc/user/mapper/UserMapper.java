package com.mvc.user.mapper;

import com.mvc.user.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qyc
 */
public interface UserMapper extends Mapper<User> {
    /**
     * selectMemberByGroupId
     *
     * @param groupId
     * @return
     */
    List<User> selectMemberByGroupId(@Param("groupId") int groupId);

    /**
     * selectLeaderByGroupId
     *
     * @param groupId
     * @return
     */
    List<User> selectLeaderByGroupId(@Param("groupId") int groupId);
}