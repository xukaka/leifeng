package io.renren.modules.app.controller.banner;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.BannerDto;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.form.BannerForm;
import io.renren.modules.app.form.BannerUpdateForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.BannerService;
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
@RequestMapping("/app/banner")
@Api(tags = "横幅")
public class BannerController {
    @Autowired
    private BannerService bannerService;


    @GetMapping("/list")
    @ApiOperation("分页获取横幅列表")
    public R getBanners(@RequestParam BannerTypeEnum type, @RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<BannerDto> banners = bannerService.getBanners(type, page);
        return R.ok().put("result", banners);
    }

    @PostMapping("/create")
    @ApiOperation("创建横幅")
    public R createBanner(@RequestBody BannerForm form) {
        bannerService.createBanner( form);
        return R.ok();
    }


    @GetMapping("/detail")
    @ApiOperation("获取横幅详情")
    public R getBanner(@RequestParam Long id) {
        BannerDto banner = bannerService.getBanner(id);
        return R.ok().put("result", banner);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除横幅")
    public R deleteBanner(@RequestParam Long id) {
        bannerService.deleteBanner(id);
        return R.ok();
    }

    @PutMapping("/update")
    @ApiOperation("更新横幅信息")
    public R updateBanner(@RequestBody BannerUpdateForm form) {
        bannerService.updateBanner(form);
        return R.ok();
    }

}
