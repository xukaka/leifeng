package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.entity.CircleAuditStatusEnum;
import io.renren.modules.app.entity.task.TaskCircleEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskCircleForm;
import io.renren.modules.app.form.TaskCircleUpdateForm;
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
    public R getCircles(@RequestParam String keyword, @RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskCircleDto> circles = taskCircleService.getCircles(ReqUtils.currentUserId(),keyword,page);
        return R.ok().put("result", circles);
    }

    @Login
    @GetMapping("/list/myJoined")
    @ApiOperation("分页获取我加入的任务圈列表")
    public R getMyJoinedCircles(@RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskCircleDto> circles = taskCircleService.getMyJoinedCircles(ReqUtils.currentUserId(),page);
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
    public R updateCircle(@RequestBody TaskCircleUpdateForm form) {
        taskCircleService.updateCircle(form);
        return R.ok();
    }

    @Login
    @DeleteMapping("/dismiss/{id}")
    @ApiOperation("解散任务圈")
    public R dismissCircle(@PathVariable("id") Long id) {
        taskCircleService.dismissCircle(ReqUtils.currentUserId(),id);
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

    @Login
    @GetMapping("/audit")
    @ApiOperation("入圈审核")
    public R audit(@RequestParam("auditId") Long auditId,@RequestParam("status")CircleAuditStatusEnum status) {
        taskCircleService.audit(auditId, status);
        return R.ok();
    }

    @GetMapping("/members")
    @ApiOperation("分页获取圈成员列表")
    public R searchCircleMembers(@RequestParam Long circleId,@RequestParam String keyword,@RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<MemberDto> members=taskCircleService.getCircleMembers(circleId,keyword, page);
            return R.ok().put("result", members);
    }


}
