package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/app/task")
@Api(tags = "任务")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @PostMapping("/create")
    @ApiOperation("创建任务")
    public R createTask(@RequestBody TaskForm form) {
        taskService.createTask(form);
        return R.ok();
    }

    @PostMapping("/list")
    @ApiOperation("分页获取任务列表")
    @ApiImplicitParam(name = "params", value = "分页page从1开始，sidx和order为排序字段非必填（默认按照排序字段orderNum从小大排序）", example = "{\"page\":1,\"limit\":10,\"sidx\":\"id\",\"order\":\"asc\"}")
    public R getTasks(@RequestParam Map<String, Object> params) {
        PageUtils page = taskService.queryPage(params);
        return R.ok().put("page", page);
    }


    @GetMapping("/detail/{id}")
    @ApiOperation("获取任务详细信息")
    public R getTask(@PathVariable("id") Long id) {
        TaskDto task = taskService.getTask(id);
        return R.ok().put("task", task);
    }


    @PutMapping("/update")
    @ApiOperation("更新任务信息")
    public R updateTask(@RequestBody TaskForm form) {
        taskService.updateTask(form);
        return R.ok();
    }


    @DeleteMapping("/delete")
    @ApiOperation("批量删除任务")
    public R deleteTasks(@RequestBody Long[] ids) {
        taskService.deleteTasks(ids);
        return R.ok();
    }

}
