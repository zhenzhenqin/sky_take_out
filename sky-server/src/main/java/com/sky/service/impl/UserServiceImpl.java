package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //调用微信接口服务获取openid
        //自己翻微信开发文档 https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html

        //根据开发文档 前端调用wx.login()获取登录凭证code，传回服务器后端
        //后端调用相应接口，换取用户唯一表示openid.........
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type", "authorization_code");
        String result = HttpClientUtil.doGet(WX_LOGIN_URL, map); //返回的是一个json格式数据
        JSONObject jsonObject = JSON.parseObject(result); //将json格式数据转化为内置对象

        // 检查微信是否返回了错误码
        /*if (jsonObject.containsKey("errcode") && jsonObject.getInteger("errcode") != 0) {
            String errmsg = jsonObject.getString("errmsg");
            throw new LoginFailedException("微信登录失败，错误码：" + jsonObject.getInteger("errcode") + "，错误信息：" + errmsg);
        }*/

        String openid = jsonObject.getString("openid"); //获取到返回的openid

        //判断openid是否为空 抛出异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断是否为新用户
        User user = userMapper.getByOpenid(openid);
        if (user == null) {

            //是新用户 就插入数据库
            user = User.builder()
                    .openid(openid)
                    .build();
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        }

        return user;
    }
}