package io.renren.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

public class GeoUtils {

    private static Logger logger = LoggerFactory.getLogger(GeoUtils.class);

    private static String url = "http://api.map.baidu.com/geocoder/v2/";

    public static String getGeoLocation(String lat,String lnt) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder(url);
        builder.addParameter("location",lat+","+lnt);
        builder.addParameter("output","json");
        builder.addParameter("ak","WEc8RlPXzSifaq9RHxE1WW7lRKgbid6Y");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(builder.build());
        CloseableHttpResponse response = httpClient.execute(httpGet);

        String resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        logger.info(resultString);

        JSONObject locatObject = JSONObject.parseObject(resultString);
        if(locatObject.getInteger("status")!=null && locatObject.getInteger("status")==0){
            JSONObject result = locatObject.getJSONObject("result");
            if(result.getString("formatted_address")!=null){
                return result.getString("formatted_address");
            }
        }
        return "";
    }

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
