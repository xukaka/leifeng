package io.renren.modules.app.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ReqUtils {

    public static Long currentUserId(){
       return (Long)getRequest().getAttribute("userId");
    }

    public static HttpServletRequest getRequest(){

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request;

    }


}
