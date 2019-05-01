package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.entity.task.TaskCircleEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskCircleForm;
import io.renren.modules.app.service.TaskCircleService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/app/circle")
@Api(tags = "任务圈")
public class TaskCircleController {

    @Autowired
    private TaskCircleService taskCircleService;

    @Login
    @PostMapping("/create")
    @ApiOperation("创建任务圈")
    public R createCircle(@RequestBody TaskCircleForm form) {
        taskCircleService.createCircle(ReqUtils.currentUserId(), form);
        return R.ok();
    }

    @GetMapping("/list")
    @ApiOperation("分页获取任务圈列表")
    public R getTasks(@RequestParam String cricleName, @RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskCircleDto> circles = taskCircleService.getCircles(cricleName,page);
        return R.ok().put("result", circles);
    }


    @GetMapping("/detail/{id}")
    @ApiOperation("获取任务圈详情")
    public R getCircle(@PathVariable("id") Long id) {
        TaskCircleDto circle = taskCircleService.getCircle(id);
        return R.ok().put("result", circle);
    }

    @Login
    @PutMapping("/update")
    @ApiOperation("更新任务圈信息")
    public R updateCircle(@RequestBody TaskCircleForm form) {
        taskCircleService.updateCircle(form);
        return R.ok();
    }

    @Login
    @DeleteMapping("/dismiss/{id}")
    @ApiOperation("解散任务圈")
    public R dismissCircle(@PathVariable("id") Long id) {
        taskCircleService.dismissCircle(id);
        return R.ok();
    }


    @Login
    @GetMapping("/join")
    @ApiOperation("加入任务圈")
    public R joinCircle(@RequestParam("circleId") Long circleId) {
        taskCircleService.joinCircle(ReqUtils.currentUserId(), circleId);
        return R.ok();
    }

    @Login
    @GetMapping("/exit")
    @ApiOperation("退出任务圈")
    public R exitCircle(@RequestParam("circleId") Long circleId) {
        taskCircleService.exitCircle(ReqUtils.currentUserId(), circleId);
        return R.ok();
    }
}
