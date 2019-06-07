package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.dto.TaskBannerDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.LikeTypeEnum;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.form.TaskQueryForm;
import io.renren.modules.app.service.LikeService;
import io.renren.modules.app.service.TaskService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/app/task")
@Api(tags = "任务")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private LikeService likeService;

    @GetMapping("/banner")
    @ApiOperation("获取任务横幅列表")
    public R getBanners() {
        List<TaskBannerDto> banners = taskService.getTaskBanners();
        return R.ok().put("result", banners);
    }

    @Login
    @PostMapping("/create")
    @ApiOperation("创建任务")
    public R createTask(@RequestBody TaskForm form) {
        taskService.createTask(ReqUtils.curMemberId(), form);
        return R.ok();
    }

    @Login
    @PostMapping("/publish")
    @ApiOperation("发布任务")
    public R publishTask(Long taskId){
        taskService.publishTask(taskId);
        return R.ok();
    }

    @Login
    @GetMapping("/detail/{id}")
    @ApiOperation("获取任务详细信息")
    public R getTask(@PathVariable("id") Long id) {
        TaskDto task = taskService.getTask(ReqUtils.curMemberId(), id);
        return R.ok().put("result", task);
    }


    @Login
    @PutMapping("/update")
    @ApiOperation("更新任务信息")
    public R updateTask(@RequestBody TaskForm form) {
        taskService.updateTask(form);
        return R.ok();
    }


    @Login
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除任务")
    public R deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return R.ok();
    }

    @GetMapping("/receiver/list")
    @ApiOperation("分页获取任务领取人列表")
    public R getTaskReceivers(Long taskId, Integer curPage, Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<MemberDto> tasks = taskService.getTaskReceivers(taskId, page);
        return R.ok().put("result", tasks);
    }

    @Login
    @GetMapping("/receiver/choose")
    @ApiOperation("选择任务领取人")
    public R chooseTaskReceiver(Long taskId, Long receiverId) {
        taskService.chooseTaskReceiver(taskId,ReqUtils.curMemberId(), receiverId);
        return R.ok();
    }

    @Login
    @GetMapping("/receive")
    @ApiOperation("领取任务")
    public R receiveTask(Long taskId) {
        taskService.receiveTask(ReqUtils.curMemberId(), taskId);
        return R.ok();
    }

    @Login
    @GetMapping("/execute")
    @ApiOperation("执行任务")
    public R executeTask(Long taskId) {
        taskService.executeTask(taskId, ReqUtils.curMemberId());
        return R.ok();
    }

    @Login
    @GetMapping("/receiverCancel")
    @ApiOperation("领取人取消任务")
    public R cancelTaskByReceiver(Long taskId) {
        taskService.cancelTaskByReceiver(ReqUtils.curMemberId(), taskId);
        return R.ok();
    }

    @Login
    @GetMapping("/publisherCancel")
    @ApiOperation("发布人取消任务")
    public R cancelTaskByPublisher(Long taskId) {
        taskService.cancelTaskByPublisher(ReqUtils.curMemberId(), taskId);
        return R.ok();
    }

/*    @Login
    @GetMapping("/republish")
    @ApiOperation("重新发布任务")
    public R republishTask(Long taskId) {
        taskService.republishTask(ReqUtils.curMemberId(), taskId);
        return R.ok();
    }*/


    @Login
    @GetMapping("/submit")
    @ApiOperation("提交任务")
    public R submitTask(Long taskId) {
        taskService.submitTask(ReqUtils.curMemberId(), taskId);
        return R.ok();
    }

    @Login
    @GetMapping("/complete")
    @ApiOperation("确认完成任务")
    public R completeTask(Long receiverId, Long taskId) {
        taskService.completeTask(ReqUtils.curMemberId(), receiverId, taskId);
        return R.ok();
    }


    @GetMapping("/receive/list")
    @ApiOperation("分页获取领取任务列表")
    public R getReceivedTasks(Long receiverId, Integer curPage, Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskDto> tasks = taskService.getReceivedTasks(receiverId, page);
        return R.ok().put("result", tasks);
    }

    @GetMapping("/publish/list")
    @ApiOperation("分页获取发布任务列表")
    public R getPublishedTasks(Long publisherId, Integer curPage, Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskDto> tasks = taskService.getPublishedTasks(publisherId, page);
        return R.ok().put("result", tasks);
    }

    //    @Login
    @PostMapping("/search/list")
    @ApiOperation("搜索任务列表-分页")
    public R searchTasks(@RequestBody TaskQueryForm form) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", form.getCurPage());
        pageMap.put("size", form.getPageSize());
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskDto> tasks = taskService.searchTasks(form, page);
        return R.ok().put("result", tasks);
    }

    @Login
    @GetMapping("/like")
    @ApiOperation("任务点赞")
    public R like(Long taskId) {
        likeService.like(ReqUtils.curMemberId(), taskId, LikeTypeEnum.task);
        return R.ok();
    }

    @Login
    @GetMapping("/unlike")
    @ApiOperation("取消任务点赞")
    public R unlike(Long taskId) {
        likeService.unlike(ReqUtils.curMemberId(), taskId, LikeTypeEnum.task);
        return R.ok();
    }

}
