package io.renren.modules.app.utils;

import org.apache.commons.compress.archivers.sevenz.CLI;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpClientUtil {
   private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

   public static String postExecute(HttpPost httpPost) throws IOException {
       //此处httpclient做成局部变量，因为单例的client在连续请求时会导致connect占用，之后的链接会等待
       CloseableHttpClient httpClient = HttpClients.createDefault();
       CloseableHttpResponse response = null;
       String result=null;
       RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10 * 1000).setConnectTimeout(10 * 1000).build();
       httpPost.setConfig(requestConfig);
       try {
           response = httpClient.execute(httpPost);
           result = EntityUtils.toString(response.getEntity(), "UTF-8");
       }finally {
           if (response != null) {
               response.close();
           }
           httpClient.close();
       }

       return  result;
   }
}
