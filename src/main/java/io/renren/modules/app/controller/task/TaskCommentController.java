package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.TaskCommentDto;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskAddressForm;
import io.renren.modules.app.service.TaskAddressService;
import io.renren.modules.app.service.TaskCommentService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/app/comment")
@Api(tags = "任务评论")
public class TaskCommentController {

    @Autowired
    private TaskCommentService taskCommentService;

    @Login
    @PostMapping("/add")
    @ApiOperation("新增评论")
    public R addComment(@RequestParam Long taskId, @RequestParam String content) {
        taskCommentService.addComment(taskId,ReqUtils.currentUserId(), content);
        return R.ok();
    }

    @GetMapping("/list")
    @ApiOperation("分页获取评论列表")
    public R getComments(@RequestParam Long taskId,@RequestParam Integer curPage,@RequestParam Integer pageSize) {
        Map<String,Object> pageMap = new HashMap<>();
        pageMap.put("page",curPage);
        pageMap.put("size",pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskCommentDto> comments = taskCommentService.getComments(taskId,page);
        return R.ok().put("result", comments);
    }



    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除评论")
    public R deleteComment(@PathVariable("id") Long id) {
        taskCommentService.deleteComment(id);
        return R.ok();
    }

}
