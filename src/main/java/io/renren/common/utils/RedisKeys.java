package io.renren.common.utils;

/**
 * Redis所有Keys
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-18 19:51
 */
public class RedisKeys {

    public static String BANNER_KEY = "redis:task:banner:key";
    public static String PHONE_CODE_KEY = "redis:validate:phonecode:key:";//短信验证

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }

}
