package com.mvc.user.rest;

import com.github.pagehelper.PageInfo;
import com.mvc.auth.common.constatns.CommonConstants;
import com.mvc.common.msg.*;
import com.mvc.common.util.Query;
import com.mvc.user.service.UserService;
import com.mvc.user.dto.*;
import com.mvc.user.entity.User;
import com.mvc.auth.client.annotation.IgnoreClientToken;
import com.mvc.auth.client.annotation.IgnoreUserToken;
import com.mvc.common.context.BaseContextHandler;
import com.mvc.common.rest.BaseController;
import com.mvc.user.rpc.service.AuthService;
import com.mvc.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@ComponentScan
public class UserController extends BaseController<UserService,User> {
    @Value("${auth.client.id}")
    protected String clientId;
    @Value("${ethnum.key}")
    protected String ethnumKey;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private RedisTemplate redisTemplate;

    // 用户登录
    @IgnoreUserToken
    @IgnoreClientToken
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public @ResponseBody  BaseResponse login(@RequestBody @Valid UserDTO user,HttpSession session) throws Exception {
        String result = userService.login(session, user);
        return new BaseResponse(200, result);
    }


    // 用户注册
    @IgnoreUserToken
    @IgnoreClientToken
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public @ResponseBody Result createUser(@RequestBody @Valid UserDTO user) throws UnsupportedEncodingException {
        User userBean = userService.insert(user);
        Result<String> result = authService.createUserAuthenticationToken(new TokenDTO(userBean.getCellphone(), clientId, userBean.getId(), userBean.getAddressEth()));
        userService.updateUserAddress(userBean);
        return ResultGenerator.genSuccessResult(result.getData());
    }

    // 忘记密码
    @IgnoreUserToken
    @IgnoreClientToken
    @RequestMapping(value = "forget", method = RequestMethod.POST)
    public @ResponseBody BaseResponse forget(@RequestBody @Valid ForgetDTO user) throws UnsupportedEncodingException {
        String result = userService.forget(user);
        return new BaseResponse(200, result);
    }

    // 修改密码
    @RequestMapping(value = "pwd", method = RequestMethod.POST)
    public @ResponseBody BaseResponse updatePwd(@RequestBody @Valid UpdateDTO updateDTO) throws UnsupportedEncodingException {
        updateDTO.setCellphone(BaseContextHandler.getUsername());
        String result = userService.updatePwd(updateDTO);
        return new BaseResponse(200, result);
    }

    // 修改手机号
    @RequestMapping(value = "cellPhone", method = RequestMethod.POST)
     public @ResponseBody BaseResponse updatePhone(@RequestBody @Valid UpdatePhoneDTO updatePhoneDTO) throws UnsupportedEncodingException {
         updatePhoneDTO.setCellphone(BaseContextHandler.getUsername());
         String result = userService.updatePhone(updatePhoneDTO);
         return new BaseResponse(200, result);
     }

    // 获取充值地址[暂时只支持以太坊地址]
    @RequestMapping(value = "address", method = RequestMethod.POST)
     public @ResponseBody BaseResponse updatePhone(){
         String result = userService.getAddress(BaseContextHandler.getUsername());
         return new BaseResponse(200, result);
     }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public @ResponseBody
    Result<PageInfo<UserVO>> getList(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        return ResultGenerator.genSuccessResult(userService.list(query, (String)params.get("cellphone")));
    }

    @RequestMapping(value = "ethnumKey", method = RequestMethod.GET)
    public @ResponseBody
    Result<String> getEthnumKey(){
        return ResultGenerator.genSuccessResult(userService.getEthnumKey());
    }

    // 校验密码
    @RequestMapping(value = "checkPwd", method = RequestMethod.POST)
    public @ResponseBody Result checkPwd(@RequestBody @Valid PwdCheckDTO pwdCheckDTO){
        userService.checkPwd(pwdCheckDTO);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    @ResponseBody
    public ObjectRestResponse<User> update(@RequestBody User entity) throws UnsupportedEncodingException {
        super.update(entity);
        User user = userService.selectById(entity.getId());
        if (null != user) {
            redisTemplate.opsForValue().set(CommonConstants.USER_STATUS + user.getCellphone(), user.getStatus());
        }
        return new ObjectRestResponse();
    }
}
