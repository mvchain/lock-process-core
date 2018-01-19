package com.mvc.user.mapper;

import com.mvc.user.entity.Group;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author qyc
 */
public interface GroupMapper extends Mapper<Group> {
    /**
     * deleteGroupMembersById
     *
     * @param groupId
     */
    void deleteGroupMembersById(@Param("groupId") int groupId);

    /**
     * deleteGroupLeadersById
     *
     * @param groupId
     */
    void deleteGroupLeadersById(@Param("groupId") int groupId);

    /**
     * insertGroupMembersById
     *
     * @param groupId
     * @param userId
     */
    void insertGroupMembersById(@Param("groupId") int groupId, @Param("userId") int userId);

    /**
     * insertGroupLeadersById
     *
     * @param groupId
     * @param userId
     */
    void insertGroupLeadersById(@Param("groupId") int groupId, @Param("userId") int userId);
}