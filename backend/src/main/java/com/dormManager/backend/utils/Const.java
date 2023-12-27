package com.dormManager.backend.utils;

public class Const {

    /**
     * JWT黑名单缓存键前缀。
     */
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";

    /**
     * 邮箱验证限制缓存键前缀。
     */
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";

    /**
     * 邮箱验证数据缓存键前缀。
     */
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";

    /**
     * 用于指定CORS（跨域资源共享）过滤器的排序顺序。
     */
    public static final int ORDER_CORS = -102;
}
