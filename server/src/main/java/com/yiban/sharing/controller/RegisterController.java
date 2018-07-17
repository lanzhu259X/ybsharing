package com.yiban.sharing.controller;

import com.yiban.sharing.constants.IdentifierType;
import com.yiban.sharing.constants.UserStatus;
import com.yiban.sharing.dao.UserAuthMapper;
import com.yiban.sharing.dao.UserMapper;
import com.yiban.sharing.dto.RegisterBody;
import com.yiban.sharing.entities.User;
import com.yiban.sharing.entities.UserAuth;
import com.yiban.sharing.exception.BizException;
import com.yiban.sharing.exception.BizRuntimeException;
import com.yiban.sharing.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
@RestController
public class RegisterController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthMapper userAuthMapper;


    /**
     * 注册
     * @param body
     * @return
     * @throws Exception
     */
    @Transactional
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody RegisterBody body) throws Exception {
        if (body == null) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(body.getPhone())) {
            log.warn("register but phone is empty.");
            throw new BizException(ErrorCode.PHONE_EMPTY);
        }
        if (StringUtils.isEmpty(body.getPassword())) {
            log.warn("register but password is empty.");
            throw new BizException(ErrorCode.PASSWORD_EMPTY);
        }

        // validate phone was register ?
        User phoneUser = userMapper.findByPhone(body.getPhone());
        if (phoneUser != null) {
            log.warn("register user but phone was registered, {}", body.getPhone());
            throw new BizException(ErrorCode.PHONE_WAS_REGISTERED);
        }

        //create user info
        User user = new User();
        user.setPhone(body.getPhone());
        user.setUserName(body.getUserName());
        user.setStatus(UserStatus.NORMAL.name());
        user.setCreatedBy(body.getPhone());
        user.setCreatedTime(new Date());
        int userCount = userMapper.insert(user);
        if (userCount <= 0 || user.getId() == null) {
            log.error("register user but create user info fail.");
            throw new BizRuntimeException(ErrorCode.DB_INSER_FAIL);
        }

        //create user auth info
        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(user.getId());
        userAuth.setIdentifierType(IdentifierType.PHONE.name());
        userAuth.setIdentifier(body.getPhone());
        userAuth.setCredential(new BCryptPasswordEncoder().encode(body.getPassword()));
        userAuth.setCreatedTime(new Date());
        userAuth.setCreatedBy(String.valueOf(user.getId()));
        int authCount = userAuthMapper.insert(userAuth);
        if (authCount <= 0) {
            log.error("register user but create user auth fail.");
            throw new BizRuntimeException(ErrorCode.DB_INSER_FAIL);
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "logoff", method = RequestMethod.GET)
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.info("user:{} logout system.", auth.getPrincipal());
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().build();
    }

}
