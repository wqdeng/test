/*
 * @(#) LoginAction.java
 * @Author:wqdeng(mail) 2014－8－17
 * @Copyright (c) 2002-2014 wqdeng Limited. All rights reserved.
 */
package com.wqdeng.wqdeng.web;

import org.apache.commons.lang3.StringUtils;

/**
  * @author wqdeng(mail) 2014－8－17
  * @version 1.0
  * @Function 类功能说明
  */
public class LoginAction {

    /**
     * 用户名
     */
    private String userName = StringUtils.EMPTY;

    /**
     * 密码
     */
    private String password = StringUtils.EMPTY;

    /**
     * 
      * login(用户登录)
      * @return
     */
    public String login() {
        if (!StringUtils.isNotBlank(userName) && !StringUtils.isNotBlank(password)) {
            return "login";
        }

        if ("ssh".equalsIgnoreCase(userName) && "123456".equalsIgnoreCase(password)) {
            return "ok";
        }

        return "error";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
