package io.renren.common.utils;


import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    private BeanUtil() {

    }

    public static void convert(final Object dest, final Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            logger.error("对象转换异常 dest:{}, orig:{}", dest.toString(), orig.toString());
            throw new RuntimeException (e.toString());
        }
    }

    public static <T> T copy(Object srcObj, final Class<T> destClass) {

        try {
            T destObj = destClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(srcObj, destObj);
            return destObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> copy(List<?> srcObjList, final Class<T> destClass) {
        if (srcObjList == null || srcObjList.size() == 0) {
            return null;
        }
        List<T> voList = new ArrayList<>(srcObjList.size());
        try {
            for (Object srcObj : srcObjList) {
                T destObj = destClass.newInstance();
                org.springframework.beans.BeanUtils.copyProperties(srcObj, destObj);
                voList.add(destObj);
            }
            return voList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
