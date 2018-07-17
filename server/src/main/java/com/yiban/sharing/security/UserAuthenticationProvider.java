package com.yiban.sharing.security;

import com.yiban.sharing.constants.IdentifierType;
import com.yiban.sharing.constants.UserStatus;
import com.yiban.sharing.dao.UserAuthMapper;
import com.yiban.sharing.dao.UserMapper;
import com.yiban.sharing.entities.User;
import com.yiban.sharing.entities.UserAuth;
import com.yiban.sharing.exception.BizRuntimeException;
import com.yiban.sharing.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class UserAuthenticationProvider implements AuthenticationProvider {

    private UserMapper userMapper;
    private UserAuthMapper userAuthMapper;

    @Inject
    public UserAuthenticationProvider(UserMapper userMapper, UserAuthMapper userAuthMapper) {
        this.userMapper = userMapper;
        this.userAuthMapper = userAuthMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String username = authentication.getName();
        List<String> credentials = (List<String>) authentication.getCredentials();
        if (StringUtils.isEmpty(username)) {
            log.warn("username missing");
            throw new BizRuntimeException(ErrorCode.LOGIN_USERNAME_MISSING);
        }

        if (credentials == null || credentials.size() != 2) {
            log.warn("password missing.");
            throw new BizRuntimeException(ErrorCode.LOGIN_PASSWORD_INVALID);
        }
        String password = credentials.get(0);
        String openId = credentials.get(1);

        // identifier by phone
        User user = userMapper.findByPhone(username);
        if (user == null) {
            log.warn("phone {} not found", username);
            throw new BizRuntimeException(ErrorCode.USER_NOT_FOUND);
        }
        if (!UserStatus.NORMAL.name().equalsIgnoreCase(user.getStatus())) {
            log.warn("user status is not in normal status. userId:{}, status:{}", user.getId(), user.getStatus());
            throw new BizRuntimeException(ErrorCode.USER_NOT_FOUND);
        }
        // 认证逻辑
        if (new BCryptPasswordEncoder().matches(password, user.getCredential())) {
            log.info("Login in successfully, username={}", username);

            //save wx openid
            saveWxOpenId(openId, user);

            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            Authentication auth = new UsernamePasswordAuthenticationToken(user.getCompactUser(), password, grantedAuthorities);
            return auth;
        } else {
            log.warn("Login password not match, username={}", username);
            throw new BizRuntimeException(ErrorCode.LOGIN_PASSWORD_INVALID);
        }
    }

    private void saveWxOpenId(String openId, User user) {
        if (!StringUtils.isEmpty(openId)) {
            UserAuth userAuth = userAuthMapper.findByIdentifier(IdentifierType.WEIXIN.name(), openId);
            if (userAuth == null) {
                userAuth = new UserAuth();
                userAuth.setUserId(user.getId());
                userAuth.setIdentifierType(IdentifierType.WEIXIN.name());
                userAuth.setIdentifier(openId);
                userAuth.setCreatedBy(String.valueOf(user.getId()));
                userAuth.setCreatedTime(new Date());
                userAuthMapper.insert(userAuth);
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
