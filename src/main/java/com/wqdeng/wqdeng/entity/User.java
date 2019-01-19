/*
 * @(#) User.java
 * @Author:wqdeng(mail) 2014-8-17
 * @Copyright (c) 2002-2014 wqdeng Limited. All rights reserved.
 */
package com.wqdeng.wqdeng.entity;

import org.apache.commons.lang3.StringUtils;

/**
  * @author wqdeng(mail) 2014-8-17
  * @version 1.0
  * @Function 类功能说明
  */
public class User {

    private Integer id;

    private String userName = StringUtils.EMPTY;

    private String password = StringUtils.EMPTY;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
