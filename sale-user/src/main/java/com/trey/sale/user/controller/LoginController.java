package com.trey.sale.user.controller;

import com.trey.sale.user.constant.CookieConstant;
import com.trey.sale.user.constant.RedisConstant;
import com.trey.sale.user.dataobject.UserInfo;
import com.trey.sale.user.enums.ResultEnum;
import com.trey.sale.user.enums.RoleEnum;
import com.trey.sale.user.service.UserService;
import com.trey.sale.user.util.CookieUtils;
import com.trey.sale.user.util.ResultVoUtils;
import com.trey.sale.user.vo.ResultVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/buyer")
    public ResultVo buyer(@RequestParam("openid") String openid, HttpServletResponse response) {
        // 1.openid和数据库里的数据匹配
        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVoUtils.error(ResultEnum.LOGIN_FAIL);
        }

        // 2.判断角色
        if (RoleEnum.BUYER.getCode() != userInfo.getRole()) {
            return ResultVoUtils.error(ResultEnum.ROLE_ERROR);
        }

        // 3.cookie里设置openid=abc
        CookieUtils.set(response, CookieConstant.OPENID, openid, CookieConstant.expire);

        return ResultVoUtils.success();
    }

    @GetMapping("/seller")
    public ResultVo seller(@RequestParam("openid") String openid,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        // 判断是否登录
        Cookie cookie = CookieUtils.get(request, CookieConstant.TOKEN);
        if (cookie != null && !StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE, cookie.getValue())))) {
            return ResultVoUtils.success();
        }

        // 1.openid和数据库里的数据匹配
        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVoUtils.error(ResultEnum.LOGIN_FAIL);
        }

        // 2.判断角色
        if (!RoleEnum.SELLER.getCode().equals(userInfo.getRole())) {
            return ResultVoUtils.error(ResultEnum.ROLE_ERROR);
        }

        // 3.redis设置key=UUID, value=xyz
        String token = UUID.randomUUID().toString();
        Integer expire = CookieConstant.expire;
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEMPLATE, token),
                openid,
                expire,
                TimeUnit.SECONDS);

        // 4.cookie里设置openid=abc
        CookieUtils.set(response, CookieConstant.TOKEN, token, CookieConstant.expire);

        return ResultVoUtils.success();
    }
}