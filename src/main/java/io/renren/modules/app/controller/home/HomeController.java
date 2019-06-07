package io.renren.modules.app.controller.home;

import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.service.WechatService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/app/home")
@Api(tags = "首页")
public class HomeController {
    private final static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private MemberService memberService;

    @Autowired
    private WechatService wechatService;


    @Login
    @GetMapping("/checkIn")
    @ApiOperation("用户签到")
    public R checkIn() {
        Map<String, Object> result = memberService.checkIn(ReqUtils.curMemberId(), 1);
        return R.ok().put("result", result);
    }

    /**
     * 微信分享证书获取
     *
     * @param
     * @return signature
     * @throws IOException
     */
    @ApiOperation("分享签名")
    @RequestMapping(value = "/share/signature", method = RequestMethod.GET)
    public R createSignature(@RequestParam String url) {
        LOGGER.info("RestFul of createSignature parameters url:{}", url);
        try {
            Map<String,Object> rs = wechatService.createSignature(url);
            LOGGER.info("RestFul of signature is successful.", rs);
            return R.ok().put("result", rs);
        } catch (Exception e) {
            LOGGER.error("RestFul of signature is error.", e);
        }
        return R.error("signature fail.");
    }


}
