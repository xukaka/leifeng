package io.renren.modules.app.controller.home;

import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/app/home")
@Api(tags = "首页")
public class HomeController {
    @Autowired
    private MemberService memberService;



    @Login
    @GetMapping("/checkIn")
    @ApiOperation("用户签到")
    public R checkIn() {
        Map<String,Object> result=memberService.checkIn(ReqUtils.curMemberId(),1);
        return R.ok().put("result",result);
    }



}
