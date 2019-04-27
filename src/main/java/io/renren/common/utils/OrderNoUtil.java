package io.renren.common.utils;

import java.util.Date;
import java.util.Random;

public class OrderNoUtil {

    public static String generateOrderNo(){
        String orderNo = DateUtils.format(new Date(), "yyyyMMddHHmmss");
        orderNo+=new Random().nextInt(1000);
        return  orderNo;
    }
}
