package com.mvc.user.service;

import com.ace.cache.annotation.Cache;
import com.mvc.auth.client.jwt.UserAuthUtil;
import com.mvc.common.biz.BaseBiz;
import com.mvc.common.constant.UserConstant;
import com.mvc.user.entity.User;
import com.mvc.user.mapper.MenuMapper;
import com.mvc.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

/**
 * @author qyc
 * @create 2017-06-08 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertSelective(User entity) throws UnsupportedEncodingException {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        entity.setPassword(password);
        super.insertSelective(entity);
    }

    @Override
    public void updateSelectiveById(User entity) {
        userMapper.updateByPrimaryKeySelective(entity);
    }

    @Cache(key = "user{1}")
    public User getUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return mapper.selectOne(user);
    }

}
