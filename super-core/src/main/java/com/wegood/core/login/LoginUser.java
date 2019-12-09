package com.wegood.core.login;

/**
 * @author Rain
 */
public interface LoginUser {

    /**
     * 获取 auth_user 的用户id
     */
    String getUserId();

    /**
     * 用户 id, 包括微服务 msToken
     */
    String getAllUserId();

    String getTokenFrom();

    /**
     * 用户昵称
     */
    String getNickName();

    /**
     * 登录产品，从什么产品登录
     */
    String getLoginFrom();

    Integer getVersion();

}
