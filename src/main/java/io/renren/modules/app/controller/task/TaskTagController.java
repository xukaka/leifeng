package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.service.TaskService;
import io.renren.modules.app.service.TaskTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/app/tag")
@Api(tags = "任务标签")
public class TaskTagController {

    @Autowired
    private TaskTagService taskTagService;


    @PostMapping("/create")
    @ApiOperation("创建任务标签")
    public R createTask(@RequestParam String tagName) {
        taskTagService.createTaskTag(tagName);
        return R.ok();
    }

/*    @PostMapping("/list")
    @ApiOperation("分页获取任务列表")
    public R getTasks(@RequestParam Map<String, Object> params) {
        PageUtils page = taskService.queryPage(params);
        return R.ok().put("page", page);
    }*/


    @PutMapping("/update")
    @ApiOperation("更新任务标签信息")
    public R updateTaskTag(@RequestParam Long tagId, @RequestParam String tagName) {
        taskTagService.updateTaskTag(tagId, tagName);
        return R.ok();
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除任务标签")
    public R deleteTaskTag(@PathVariable("id") Long id) {
        taskTagService.deleteTaskTag(id);
        return R.ok();
    }


}
