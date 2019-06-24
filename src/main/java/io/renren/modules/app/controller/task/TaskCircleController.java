package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.PageWrapperUtils;
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
        taskCircleService.createCircle(ReqUtils.curMemberId(), form);
        return R.ok();
    }

    @Login
    @GetMapping("/list")
    @ApiOperation("分页获取任务圈列表")
    public R getCircles(String keyword, Integer curPage, Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<TaskCircleDto> circles = taskCircleService.getCircles(ReqUtils.curMemberId(), keyword, page);
        return R.ok().put("result", circles);
    }

    @Login
    @GetMapping("/list/myJoined")
    @ApiOperation("分页获取我加入的任务圈列表")
    public R getMyJoinedCircles(Integer curPage, Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<TaskCircleDto> circles = taskCircleService.getMyJoinedCircles(ReqUtils.curMemberId(), page);
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
        taskCircleService.dismissCircle(ReqUtils.curMemberId(), id);
        return R.ok();
    }


    @Login
    @GetMapping("/join")
    @ApiOperation("加入任务圈")
    public R joinCircle(Long circleId) {
        Map<String,Object> result = taskCircleService.joinCircle(ReqUtils.curMemberId(), circleId);
        return R.ok().put("result",result);
    }

    @Login
    @GetMapping("/exit")
    @ApiOperation("退出任务圈")
    public R exitCircle(Long circleId) {
        taskCircleService.exitCircle(ReqUtils.curMemberId(), circleId);
        return R.ok();
    }

    @Login
    @GetMapping("/audit")
    @ApiOperation("入圈审核")
    public R audit(Long auditId, CircleAuditStatusEnum status) {
        taskCircleService.audit(auditId, status);
        return R.ok();
    }

    @GetMapping("/members")
    @ApiOperation("分页获取圈成员列表")
    public R searchCircleMembers(Long circleId, String keyword, Integer curPage, Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<MemberDto> members = taskCircleService.getCircleMembers(circleId, keyword, page);
        return R.ok().put("result", members);
    }


}
