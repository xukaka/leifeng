package io.renren.modules.app.utils;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

public final class LocationUtils {
    /**
     * 根据当前坐标，范围计算最大最小坐标点
     *
     * @param latitude  当前位置的维度
     * @param longitude 当前位置的经度
     * @param raidus    范围半径
     * @return
     */
    public static Map<String, Double> getAround(double latitude, double longitude, int raidus) {
        double degree = (24901 * 1609) / 360.0;
        double dpmLat = 1 / degree;
        double radiusLat = dpmLat * raidus;
        double minLat = latitude - radiusLat;
        double maxLat = latitude + radiusLat;
        double mpdLng = degree * cos(latitude * (PI / 180));
        double dpmLng = 1 / mpdLng;
        double radiusLng = dpmLng * raidus;
        double minLng = longitude - radiusLng;
        double maxLng = longitude + radiusLng;
        Map<String, Double> aroundMap = new HashMap<>();
        aroundMap.put("minLat", minLat);
        aroundMap.put("maxLat", maxLat);
        aroundMap.put("minLng", minLng);
        aroundMap.put("maxLng", maxLng);
        return aroundMap;
    }


    /**
     * 计算两点之间的距离
     * @param fromLat 开始点的维度
     * @param fromLng 开始点的经度
     * @param toLat 结束点的维度
     * @param toLng 结束点的经度
     * @return 距离，单位m
     */
    public long getDistance(double fromLat, double fromLng, double toLat, double toLng) {
        int earthRadius = 6367000;//地球半径
        fromLat = (fromLat * PI) / 180;
        fromLng = (fromLng * PI) / 180;
        toLat = (toLat * PI) / 180;
        toLng = (toLng *PI) / 180;
        double calcLongitude = toLng - fromLng;
        double calcLatitude = toLat - fromLat;
        double stepOne = pow(sin(calcLatitude / 2), 2) + cos(fromLat) * cos(toLat) *pow(sin(calcLongitude / 2), 2);
        double stepTwo = 2 * asin(min(1, sqrt(stepOne)));
        double calculatedDistance = earthRadius * stepTwo;
        return round(calculatedDistance);
    }

}
