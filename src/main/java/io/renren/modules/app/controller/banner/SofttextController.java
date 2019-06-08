package io.renren.modules.app.controller.banner;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.dto.BannerDto;
import io.renren.modules.app.dto.SofttextDto;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.form.*;
import io.renren.modules.app.service.BannerService;
import io.renren.modules.app.service.SofttextService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/app/softtext")
@Api(tags = "软文")
public class SofttextController {
    @Autowired
    private SofttextService softtextService;


    @GetMapping("/list")
    @ApiOperation("分页获取软文列表")
    public R getSofttexts(Integer curPage,  Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<SofttextDto> softtexts = softtextService.getSofttexts(page);
        return R.ok().put("result", softtexts);
    }

    @PostMapping(value = "/create"/*,produces="text/html;charset=UTF-8"*/)
    @ApiOperation("创建软文")
    public R createSofttext(@RequestBody SofttextForm form) {
        softtextService.createSofttext( form);
        return R.ok();
    }


    @GetMapping("/detail")
    @ApiOperation("获取软文详情")
    public R getSofttext( Long id) {
        SofttextDto softtext = softtextService.getSofttext(id);
        return R.ok().put("result", softtext);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除软文")
    public R deleteSofttext( Long id) {
        softtextService.deleteSofttext(id);
        return R.ok();
    }

    @PutMapping("/update")
    @ApiOperation("更新软文信息")
    public R updateSofttext(@RequestBody SofttextUpdateForm form) {
        softtextService.updateSofttext(form);
        return R.ok();
    }

}
