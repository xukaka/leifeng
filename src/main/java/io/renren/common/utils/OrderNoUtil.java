package io.renren.common.utils;

import java.util.Date;
import java.util.Random;

public class OrderNoUtil {

    public static String generateOrderNo(Long taskId){
        String orderNo = DateUtils.format(new Date(), "yyyyMMddHHmmss");
        orderNo+=taskId;
        return  orderNo;
    }
}
