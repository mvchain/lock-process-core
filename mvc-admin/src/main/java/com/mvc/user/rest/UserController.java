package com.mvc.user.rest;

import com.alibaba.fastjson.JSONObject;
import com.mvc.auth.client.annotation.IgnoreUserToken;
import com.mvc.common.constant.RestMsgConstants;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import com.mvc.common.rest.BaseController;
import com.mvc.user.entity.JwtAuthenticationRequest;
import com.mvc.user.entity.User;
import com.mvc.user.rpc.AuthService;
import com.mvc.user.rpc.service.PermissionService;
import com.mvc.user.service.UserBiz;
import com.mvc.user.vo.FrontUser;
import com.mvc.user.vo.MenuTree;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author qyc
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController<UserBiz, User> {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value = "/front/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserInfo(@RequestHeader("authorization") String token) throws Exception {
        FrontUser userInfo = permissionService.getUserInfo(token);
        if (userInfo == null) {
            return ResponseEntity.status(401).body(false);
        } else {
            return ResponseEntity.ok(userInfo);
        }
    }

    @RequestMapping(value = "/front/menus", method = RequestMethod.GET)
    public @ResponseBody
    List<MenuTree> getMenusByUsername(String token) throws Exception {
        return permissionService.getMenusByUsername(token);
    }

    @IgnoreUserToken
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public @ResponseBody
    Result createUser(@RequestBody User user) throws UnsupportedEncodingException {
        userBiz.insertSelective(user);
        return ResultGenerator.genSuccessResult();
    }


    @IgnoreUserToken
    @RequestMapping(value = "token", method = RequestMethod.POST)
    public Result<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpSession session) throws Exception {
        boolean result = StringUtils.equalsIgnoreCase(String.valueOf(session.getAttribute("imageCode")), authenticationRequest.getValiCode());
        Assert.isTrue(result, RestMsgConstants.VALI_IMG_ERR);
        ResponseEntity<JSONObject> resp = authService.createAuthenticationToken(authenticationRequest);
        return ResultGenerator.genSuccessResult(resp.getBody().get("data"));
    }

}
