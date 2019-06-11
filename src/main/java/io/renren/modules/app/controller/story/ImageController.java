package io.renren.modules.app.controller.story;

import io.renren.common.exception.RRException;
import io.renren.common.utils.Constant;
import io.renren.common.utils.R;
import io.renren.modules.oss.cloud.OSSFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ApiOperation("图片上传")
    public R upload(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }

        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
        logger.info("upload image,get url:" + url);

        return R.ok().put("result", url + Constant.IMAGE_STYLE);
    }

}
