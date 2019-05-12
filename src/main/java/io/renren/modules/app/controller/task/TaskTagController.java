package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.entity.task.TaskTagEntity;
import io.renren.modules.app.service.TaskTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/app/tag")
@Api(tags = "任务标签")
public class TaskTagController {

    @Autowired
    private TaskTagService taskTagService;


    @PostMapping("/create")
    @ApiOperation("创建任务标签")
    public R createTag(@RequestParam String tagName) {
        taskTagService.createTag(tagName);
        return R.ok();
    }

    @GetMapping("/list")
    @ApiOperation("分页获取任务标签列表")
    @ApiImplicitParam(name="curPage",required = true,value = "分页page从1开始",dataType = "Integer",paramType = "query")
    public R getTags(@RequestParam Integer curPage,@RequestParam Integer pageSize) {
        Map<String,Object> pageMap = new HashMap<>();
        pageMap.put("page",curPage);
        pageMap.put("limit",pageSize);
        PageUtils<TaskTagEntity> tags = taskTagService.getTags(pageMap);
        return R.ok().put("result", tags);
    }

    @GetMapping("/all")
    @ApiOperation("获取所有任务标签列表")
    public R getAllTags() {
        List<TaskTagEntity> tags = taskTagService.getAllTags();
        return R.ok().put("result", tags);
    }

    @PutMapping("/update")
    @ApiOperation("更新任务标签信息")
    public R updateTaskTag(@RequestParam Long tagId, @RequestParam String tagName) {
        taskTagService.updateTag(tagId, tagName);
        return R.ok();
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除任务标签")
    public R deleteTaskTag(@PathVariable("id") Long id) {
        taskTagService.deleteTag(id);
        return R.ok();
    }


}
