package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.entity.task.TagEntity;
import io.renren.modules.app.service.TagService;
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
@Api(tags = "标签")
public class TagController {

    @Autowired
    private TagService tagService;


    @PostMapping("/create")
    @ApiOperation("创建标签")
    public R createTag(@RequestParam String tagName) {
        tagService.createTag(tagName);
        return R.ok();
    }

    @GetMapping("/list")
    @ApiOperation("分页获取标签列表")
    @ApiImplicitParam(name = "curPage", required = true, value = "分页page从1开始", dataType = "Integer", paramType = "query")
    public R getTags(@RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("limit", pageSize);
        PageUtils<TagEntity> tags = tagService.getTags(pageMap);
        return R.ok().put("result", tags);
    }

    @GetMapping("/all")
    @ApiOperation("获取所有标签列表")
    public R getAllTags() {
        List<TagEntity> tags = tagService.getAllTags();
        return R.ok().put("result", tags);
    }

    @PutMapping("/update")
    @ApiOperation("更新标签信息")
    public R updateTag(@RequestParam Long tagId, @RequestParam String tagName) {
        tagService.updateTag(tagId, tagName);
        return R.ok();
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除标签")
    public R deleteTag(@PathVariable("id") Long id) {
        tagService.deleteTag(id);
        return R.ok();
    }


}
