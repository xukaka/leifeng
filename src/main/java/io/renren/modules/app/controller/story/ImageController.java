package io.renren.modules.app.controller.story;

import com.jfinal.i18n.Res;
import io.renren.common.exception.RRException;
import io.renren.common.utils.*;
import io.renren.modules.app.config.WXPayConfig;
import io.renren.modules.app.utils.HttpClientUtil;
import io.renren.modules.oss.cloud.OSSFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
@RestController
@RequestMapping("/app/image")
@Api(tags = "图片上传")
public class ImageController {
    private static Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private WXPayConfig wxPayConfig;
    
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ApiOperation("图片上传")
    public R upload(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //压缩图片：最大1M，循环压缩比率0.8
        byte[] imageBytes =  ImageUtils.commpressPicForScale(file.getBytes(),1024,0.8);
        //微信平台进行图片安全性校验
        if(!imgSecCheckWX(imageBytes,file.getOriginalFilename())){
            return R.error("图片安全性校验不通过");
        }

        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url = OSSFactory.build().uploadSuffix(imageBytes, suffix);
        logger.info("upload image,get url:" + url);
        return R.ok().put("result", url + Constant.IMAGE_STYLE);
    }


    public boolean imgSecCheckWX(byte[] bytes , String fileName) throws Exception {
        //获取access_token
        String accessToken = redisUtils.get("appid:"+wxPayConfig.getAppId());
        if(StringUtils.isEmpty(accessToken)){
            String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+wxPayConfig.getAppId()+"&secret="+wxPayConfig.getAppSecret();
            HttpPost atPost = new HttpPost(accessTokenUrl);
            String atResult = HttpClientUtil.postExecute(atPost);
            Map atmap = JsonUtil.JsonStr2Java(atResult, Map.class);
            accessToken = (String) atmap.get("access_token");
            Integer expireTime = (Integer) atmap.get("expires_in");
            redisUtils.set("appid:"+wxPayConfig.getAppId(),accessToken,expireTime);
        }

        if(StringUtils.isEmpty(accessToken)){
            throw new RRException("access_token值为空");
        }

        //包装multipart/formdata数据作为参数
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("media",new ByteArrayBody(bytes,fileName));
        HttpEntity mediaEntity = builder.build();

        String securl = "https://api.weixin.qq.com/wxa/img_sec_check?access_token="+accessToken;
        HttpPost secPost = new HttpPost(securl);
        secPost.setEntity(mediaEntity);
        String secResult = HttpClientUtil.postExecute(secPost);
        Map secMap = JsonUtil.JsonStr2Java(secResult, Map.class);
        Integer code = (Integer) secMap.get("errcode");
        logger.info("图片安全接口结果：{}",secResult);

        if(code!=0){
            return false;
        }
        return true;
    }

}
