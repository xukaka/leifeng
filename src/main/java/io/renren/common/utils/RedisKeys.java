package io.renren.common.utils;

/**
 * Redis所有Keys
 */
public class RedisKeys {

    public static String BANNER_KEY = "redis:task:banner:key:";
    public static String PHONE_CODE_KEY = "redis:validate:phonecode:key:";//短信验证
    public static String HOT_SEARCH = "redis:hot:search:key:";//热门搜索
    public static String CHECK_IN = "redis:checkin:key:";//签到
    public static String WX_PHONE = "redis:wx:phone:key:";//微信用户信息

    public static String RED_DOT_TASK = "redis:im:reddot:task:";//任务红点
    public static String RED_DOT_DYNAMIC = "redis:im:reddot:dynamic:";//动态红点
    public static String RED_DOT_CIRCLE = "redis:im:reddot:circle:";//圈红点


    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }

}
