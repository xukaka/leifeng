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
}
