package com.zj.hiapp.router;

/**
 * 路由权限判断标志
 *
 * @author 张锦
 */
public interface RouterFlag {

    /**
     * 需要登录
     */
    int FLAG_LOGIN = 0x01;

    /**
     * 需要实名认证
     */
    int FLAG_AUTHENTICATION = FLAG_LOGIN << 1;

    /**
     * 需要是vip
     */
    int FLAG_VIP = FLAG_AUTHENTICATION << 1;
}
