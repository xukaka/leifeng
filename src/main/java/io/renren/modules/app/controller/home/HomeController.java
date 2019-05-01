/*
package io.renren.modules.app.controller.home;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskCircleForm;
import io.renren.modules.app.service.HomeService;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.service.TaskCircleService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/app/home")
@Api(tags = "首页")
public class HomeController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private TaskCircleService taskCircleService;
    @Autowired
    private HomeService homeService;


    @Login
    @GetMapping("/checkIn")
    @ApiOperation("用户签到")
    public R checkIn() {
        Map<String,Object> result=memberService.checkIn(ReqUtils.currentUserId(),1);
        return R.ok().put("result",result);
    }

    @Login
    @GetMapping("/banner")
    @ApiOperation("首页横幅列表")
    public R getBanners() {
        homeService.getBanners();
        return R.ok();
    }

}
*/
