package com.trey.sale.user.service;

import com.trey.sale.user.dataobject.UserInfo;
import com.trey.sale.user.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserInfo findByOpenid(String openid) {
        return userInfoRepository.findByOpenid(openid);
    }
}