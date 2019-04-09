package io.renren.modules.app.controller.task;

import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.form.TaskCircleForm;
import io.renren.modules.app.service.TaskCircleService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
        taskCircleService.createCircle(ReqUtils.currentUserId(),form);
        return R.ok();
    }

   /* @PostMapping("/list")
    @ApiOperation("分页获取任务列表")
    @ApiImplicitParam(name = "params", value = "分页page从1开始，sidx和order为排序字段非必填（默认按照排序字段orderNum从小大排序）", example = "{\"page\":1,\"limit\":10,\"sidx\":\"id\",\"order\":\"asc\"}")
    public R getTasks(@RequestParam Map<String, Object> params) {
        PageUtils page = taskService.queryPage(params);
        return R.ok().put("result", page);
    }*/


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
    @PostMapping("/join")
    @ApiOperation("加入任务圈")
    public R joinCircle(@RequestParam("circleId") Long circleId) {
        taskCircleService.joinCircle(ReqUtils.currentUserId(),circleId);
        return R.ok();
    }

    @Login
    @PostMapping("/exit")
    @ApiOperation("退出任务圈")
    public R exitCircle(@RequestParam("circleId") Long circleId) {
        taskCircleService.exitCircle(ReqUtils.currentUserId(),circleId);
        return R.ok();
    }
}
