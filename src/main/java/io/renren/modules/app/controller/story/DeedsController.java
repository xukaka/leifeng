package io.renren.modules.app.controller.story;

import io.renren.common.exception.RRException;
import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.R;
import io.renren.modules.app.dto.DeedsDto;
import io.renren.modules.app.form.DeedsForm;
import io.renren.modules.app.service.DeedsService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huangshishui
 * @date 2019/4/18 22:56
 * 关于英雄事迹的入口
 **/
@RestController
@RequestMapping("/app/deeds")
public class DeedsController {

    private final  static Logger logger = LoggerFactory.getLogger(DeedsController.class);

    @Autowired
    private DeedsService deedsService;

    @PostMapping("/submitDeeds")
    @ApiOperation("提交英雄事迹")
    public R submitDeedsInfo(@RequestBody DeedsForm deedsForm){
        logger.info("【DeedsController.submitDeedsInfo】参数："+ JsonUtil.Java2Json(deedsForm));
        //要增加异常和日志处理
        try {
            deedsService.insertDeedsInfo(deedsForm);
        } catch (Exception e) {
            logger.error("增加事迹信息出现错误:"+e.getMessage());
            throw new RRException("增加事迹信息出现错误");
        }
        return R.ok();
    }
    @PostMapping("/queryListDeeds")
    @ApiOperation("查询某个用户发布的日记")
    public R queryListForDeeds(@RequestParam String userId){
        logger.info("【DeedsController.queryListForDeeds】");
        List<Map> deedMap = new ArrayList<Map>();
        List<DeedsDto> listDeeds = null;
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
    }

}
