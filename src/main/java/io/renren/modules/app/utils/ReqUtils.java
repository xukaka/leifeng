package io.renren.modules.app.utils;

import io.renren.common.exception.RRException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ReqUtils {

    public static Long curMemberId(){
        Object obj = getRequest().getAttribute("userId");
        if (obj == null){
            throw new RRException("登录过期，请重新登录");
        }
       return (Long)obj;
    }

    public static String getRemoteAddr(){
      return   getRequest().getRemoteAddr();
    }

    public static HttpServletRequest getRequest(){

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request;

    }


}
