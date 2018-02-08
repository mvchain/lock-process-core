package com.mvc.auth.client.jwt;

import com.mvc.auth.client.config.UserAuthConfig;
import com.mvc.auth.client.exception.JwtIllegalArgumentException;
import com.mvc.auth.client.exception.JwtSignatureException;
import com.mvc.auth.client.exception.JwtTokenExpiredException;
import com.mvc.auth.common.constatns.CommonConstants;
import com.mvc.auth.common.util.jwt.IJWTInfo;
import com.mvc.auth.common.util.jwt.JWTHelper;
import com.mvc.common.exception.auth.TokenErrorException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author qyc
 */
@Configuration
public class UserAuthUtil {
    @Autowired
    private UserAuthConfig userAuthConfig;
    @Autowired
    private RedisTemplate redisTemplate;

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            IJWTInfo infoFromToken = JWTHelper.getInfoFromToken(token, userAuthConfig.getPubKeyByte());
            if (null != redisTemplate) {
                Integer status = (Integer) redisTemplate.opsForValue().get(CommonConstants.USER_STATUS + infoFromToken.getUniqueName());
                Boolean checkUser = (null == status || 0 == status) && StringUtils.isNotBlank(infoFromToken.getAddress());
                if (checkUser) {
                    throw new TokenErrorException("用户已停用", com.mvc.common.constant.CommonConstants.EX_TOKEN_ERROR_CODE);
                }
            }
            return infoFromToken;
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenExpiredException("User token expired!");
        } catch (SignatureException ex) {
            throw new JwtSignatureException("User token signature error!");
        } catch (IllegalArgumentException ex) {
            throw new JwtIllegalArgumentException("User token is null or empty!");
        }
    }
}
