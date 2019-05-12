package io.renren.modules.app.controller.story;

import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.form.DiaryForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.DiaryService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jim.server.http.annotation.RequestPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangshishui
 * @date 2019/4/18 22:56
 **/
@Api(tags = "日记")
@RestController
@RequestMapping("/app/diary")
public class DiaryController {

    private final static Logger logger = LoggerFactory.getLogger(DiaryController.class);

    @Autowired
    private DiaryService diaryService;

    @Login
    @PostMapping("/create")
    @ApiOperation("创建日记")
    public R createDiary(@RequestBody DiaryForm form) {
        logger.info("参数：" + JsonUtil.Java2Json(form));
        diaryService.createDiary(ReqUtils.currentUserId(), form);
        return R.ok();
    }

    //    @Login
    @GetMapping("/detail/{id}")
    @ApiOperation("获取日记详情")
    public R getDiary(@PathVariable("id") Long id) {
        DiaryDto diary = diaryService.getDiary(id);
        return R.ok().put("result", diary);
    }

    @GetMapping("/list")
    @ApiOperation("分页获取日记列表")
    public R getDiarys(@RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<DiaryDto> diarys = diaryService.getDiarys(page);
        return R.ok().put("result", diarys);

    }

    @Login
    @GetMapping("/list/my")
    @ApiOperation("分页获取我的日记列表")
    public R getMyDiarys(@RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<DiaryDto> diarys = diaryService.getMyDiarys(ReqUtils.currentUserId(), page);
        return R.ok().put("result", diarys);

    }


}
