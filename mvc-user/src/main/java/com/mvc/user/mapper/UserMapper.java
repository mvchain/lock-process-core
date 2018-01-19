package com.mvc.user.mapper;

import com.mvc.user.dto.UserDTO;
import com.mvc.user.entity.User;
import com.mvc.user.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {
    public List<User> selectMemberByGroupId(@Param("groupId") int groupId);
    public List<User> selectLeaderByGroupId(@Param("groupId") int groupId);

    @Select("select * from user where cellphone=#{cellphone} and password=#{password}")
    User selectUserByPwd(UserDTO user);

    @Select("select * from user where cellphone=#{cellphone}")
    User selectUserByPhone(UserDTO user);

    @Select("SELECT * FROM `user`")
    List<UserVO> list();
}