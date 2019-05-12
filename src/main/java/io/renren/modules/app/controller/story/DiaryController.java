package io.renren.modules.app.controller.story;

import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.form.DiaryForm;
import io.renren.modules.app.service.DiaryService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jim.server.http.annotation.RequestPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangshishui
 * @date 2019/4/18 22:56
 **/
@Api(tags="日记")
@RestController
@RequestMapping("/app/diary")
public class DiaryController {

    private final  static Logger logger = LoggerFactory.getLogger(DiaryController.class);

    @Autowired
    private DiaryService diaryService;

    @Login
    @PostMapping("/create")
    @ApiOperation("创建日记")
    public R createDiary(@RequestBody DiaryForm form){
        logger.info("参数："+ JsonUtil.Java2Json(form));
        diaryService.createDiary(ReqUtils.currentUserId(),form);
        return R.ok();
    }

//    @Login
    @GetMapping("/detail/{id}")
    @ApiOperation("获取日记详情")
    public R getDiary(@PathVariable("id") Long id){
        DiaryDto diary = diaryService.getDiary(id);
        return R.ok().put("result",diary);
    }
    /*@PostMapping("/queryListDeeds")
    @ApiOperation("查询某个用户发布的日记")
    public R queryListForDeeds(@RequestParam String userId){
        logger.info("【DiaryController.queryListForDeeds】");
        List<Map> deedMap = new ArrayList<Map>();
        List<DiaryDto> listDeeds = null;
        try {
           listDeeds = deedsService.queryDeedsList(userId);

        } catch (Exception e) {
           logger.error("查询用户日记信息出错："+e.getMessage());
           throw new RRException("查询用户日志信息出错");
        }

        return R.ok().put("deedsList",listDeeds);
    }

    @GetMapping("/totalCount")
    @ApiOperation("事迹统计数量")
    public R totalReadCount(@RequestParam String deedsId){
        try {
            deedsService.updateReadCount(deedsId);
        } catch (Exception e) {
            logger.error("统计数量出错："+e.getMessage());
            throw new RRException("统计数量出错");
        }
        return R.ok();
    }*/

}
