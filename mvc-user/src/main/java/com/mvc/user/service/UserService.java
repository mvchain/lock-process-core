package com.mvc.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.api.vo.user.NewAccountDTO;
import com.mvc.auth.common.constatns.CommonConstants;
import com.mvc.common.biz.BaseBiz;
import com.mvc.common.context.BaseContextHandler;
import com.mvc.common.msg.Result;
import com.mvc.common.util.Query;
import com.mvc.user.dto.*;
import com.mvc.user.entity.Capital;
import com.mvc.user.entity.CoinInfo;
import com.mvc.user.entity.User;
import com.mvc.user.mapper.CapitalMapper;
import com.mvc.user.mapper.UserMapper;
import com.mvc.user.rpc.service.AuthService;
import com.mvc.user.rpc.service.EthernumRest;
import com.mvc.user.rpc.service.SmsService;
import com.mvc.user.util.CoinUtil;
import com.mvc.user.vo.UserVO;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends BaseBiz<UserMapper, User> {

    @Value("${auth.client.id}")
    protected String clientId;
    @Value("${ethnum.key}")
    protected String ethnumKey;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private EthernumRest ethernumRest;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CapitalMapper capitalMapper;

    public User insert(UserDTO user) throws UnsupportedEncodingException {
        Assert.isNull(userMapper.selectUserByPhone(user), "手机号已注册!");
        MobileValiDTO mobileValiDTO = new MobileValiDTO();
        mobileValiDTO.setMobile(user.getCellphone());
        mobileValiDTO.setValiCode(user.getValiCode());
        ResponseEntity<Boolean> checkResult = smsService.checkSms(mobileValiDTO);
        Assert.isTrue(checkResult.getBody(), "验证码错误");
        User userBean = user;
        userBean.init();
        insertSelective(userBean);
        // 插入初始数据
        insertDefaultBalance(userBean);
        return userBean;
    }

    private void insertDefaultBalance(User userBean) {
        for (Map.Entry<BigInteger, CoinInfo> map : CoinUtil.coinMap.entrySet()) {
            Capital capital = new Capital();
            capital.setUserId(userBean.getId());
            capital.setBalance(BigInteger.ZERO);
            capital.setInterest(BigInteger.ZERO);
            capital.setLocked(BigInteger.ZERO);
            capital.setStatus(1);
            capital.setCoinId(map.getKey());
            capital.setCreatedAt(new Date());
            capital.setUpdatedAt(new Date());
            capitalMapper.insert(capital);
        }
    }

    public String getEthnumKey() {
        // TODO 每个用户生成不同密码,加密
        return ethnumKey;
    }

    @Async
    public void updateUserAddress(User user) {
        // 生成以太坊地址
        NewAccountDTO newAccountDTO = new NewAccountDTO();
        newAccountDTO.setPassphrase(ethnumKey);
        Result<String> addressResp = ethernumRest.personal_newAccount(newAccountDTO);
        user.setAddressEth(addressResp.getData());
        // 添加redis事件监听
        redisTemplate.opsForValue().set(BaseContextHandler.ADDR_LISTEN_ + addressResp.getData(), user.getId());
        userMapper.updateByPrimaryKeySelective(user);
    }

    public String login(HttpSession session, UserDTO userDTO) throws Exception {
        Assert.isTrue(userDTO.getValiCode().equalsIgnoreCase(session.getAttribute("imageCode") + ""), "验证码不正确！");
        User user = userMapper.selectUserByPwd(userDTO);
        Assert.notNull(user, "用户名或密码错误");
        Assert.isTrue(user.getStatus() == 1, "用户已停用");
        Result<String> result = authService.createUserAuthenticationToken(new TokenDTO(user.getCellphone(), clientId, user.getId(), user.getAddressEth()));
        redisTemplate.opsForValue().set(CommonConstants.USER_STATUS + user.getCellphone(), user.getStatus());
        return result.getData();
    }

    public String forget(ForgetDTO userDTO) throws UnsupportedEncodingException {
        MobileValiDTO mobileValiDTO = new MobileValiDTO();
        mobileValiDTO.setMobile(userDTO.getCellphone());
        mobileValiDTO.setValiCode(userDTO.getValiCode());
        ResponseEntity<Boolean> checkResult = smsService.checkSms(mobileValiDTO);
        Assert.isTrue(checkResult.getBody(), "验证码错误");
        User user = userMapper.selectUserByPhone(userDTO);
        Assert.notNull(user, "用户不存在");
        user.setPassword(userDTO.getPassword());
        updateSelectiveById(user);
        Result<String> result = authService.createUserAuthenticationToken(new TokenDTO(user.getCellphone(), clientId, user.getId(), user.getAddressEth()));
        return result.getData();
    }

    public String updatePwd(UpdateDTO userDTO) throws UnsupportedEncodingException {
        User user = userMapper.selectUserByPwd(userDTO);
        Assert.notNull(user, "用户名或密码错误");
        user.setPassword(userDTO.getNewPassword());
        updateSelectiveById(user);
        return "success";
    }

    public String updatePhone(UpdatePhoneDTO updatePhoneDTO) throws UnsupportedEncodingException {
        User user = userMapper.selectUserByPwd(updatePhoneDTO);
        Assert.notNull(user, "用户名或密码错误");
        user.setCellphone(updatePhoneDTO.getNewCellPhone());
        updateSelectiveById(user);
        Result<String> result = authService.createUserAuthenticationToken(new TokenDTO(user.getCellphone(), clientId, user.getId(), user.getAddressEth()));
        return result.getData();
    }

    public String getAddress(String username) {
        UserDTO userDTO = new UserDTO();
        userDTO.setCellphone(username);
        User user = userMapper.selectUserByPhone(userDTO);
        return user.getAddressEth();
    }

    public PageInfo<UserVO> list(Query query) {
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<UserVO> list = userMapper.list();
        list.stream().forEach(user -> {
            Capital capital = new Capital();
            capital.setUserId(user.getId());
            user.setList(capitalMapper.list(capital));
        });
        return new PageInfo<>(list);
    }

    public void checkPwd(PwdCheckDTO pwdCheckDTO) {
        Assert.notNull(userMapper.selectByPrimaryKey(pwdCheckDTO.getUserId()), "密码错误");
    }

    public void updateAddress() {
        List<User> list = userMapper.selectNullAddr();
        list.stream().forEach(user -> {
            updateUserAddress(user);
        });
    }

}
