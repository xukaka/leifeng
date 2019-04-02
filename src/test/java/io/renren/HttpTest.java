package io.renren;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class HttpTest {

    @Test
    public void test() throws URISyntaxException, IOException {

        URIBuilder builder = new URIBuilder("http://service.winic.org:8009/sys_port/gateway/index.asp");
        builder.addParameter("id","xukaka");
        builder.addParameter("pwd","xu6789462");
        builder.addParameter("to","13301242325");
        builder.addParameter("content","【宠爱AI】您的验证码为：1234 请及时使用");


        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet(builder.build());
       // httpPost.addHeader("Accept","application/json;charset=utf-8");
       // httpPost.addHeader("content-Type","application/x-www-form-urlencoded;charset=utf-8");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
    }

    @Test
    public void test01(){
       /* List<String> arr = new ArrayList<>();
        for(String s:arr){

        }*/

       String msg = "{\"session_key\":\"lh5Cm5FQKHbNQww1hpXLAw==\",\"openid\":\"oFmV75VxrrYTHLt8DykYJqW96N8A\"}";
        JSONObject obj = JSONObject.parseObject(msg);
        Integer errcode = obj.getInteger("errcode");
        if(errcode==null)
        System.out.println(errcode);

    }
}
