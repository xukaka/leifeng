package io.renren.modules.app.controller.story;

import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.R;
import io.renren.modules.app.entity.story.PublishMessageEntity;
import io.renren.modules.app.form.PublishMessageForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.PublishMessageService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



/**
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
@RestController
@RequestMapping("app/message")
@Api(tags="朋友圈分享内容")
public class PublishMessageController {
    private final static Logger logger = LoggerFactory.getLogger(PublishMessageController.class);

    @Autowired
    private PublishMessageService publishMessageService;


    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("获取发布内容列表")
    @ApiImplicitParam(name="params",value = "分页page从1开始")
    public R list(@RequestParam Map<String, Object> params){
        logger.info("[PublishMessageController.list]请求参数{}", params);
        PageWrapper page = new PageWrapper(params);
        List<PublishMessageEntity> list = publishMessageService.getPage(page);
        return R.ok().put("data", list)
                .put("page",page.getCurrPage())
                .put("size",page.getPageSize());
    }


    /**
     * 信息
     */
    @GetMapping("/info")
    @ApiOperation("获取发布内容详细内容")
    public R info(Long id){
        logger.info("[PublishMessageController.info] 请求参数id={}", id);
        PublishMessageEntity publishMessage = publishMessageService.getById(id);
        return R.ok().put("data", publishMessage);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存发布内容")
    public R save(@RequestBody PublishMessageForm publishMessageForm){
        logger.info("[PublishMessageController.info] friendShareMessage={}", JsonUtil.Java2Json(publishMessageForm));
        PublishMessageEntity publishMessageEntity = new PublishMessageEntity();
        BeanUtils.copyProperties(publishMessageForm, publishMessageEntity);
        publishMessageEntity.setCreateTime(System.currentTimeMillis());
        publishMessageService.insertMsgAndTimeline(publishMessageEntity);
        return R.ok();
    }


    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation("删除朋友圈内容")
    public R delete(Long id){
        logger.info("[PublishMessageController.delete] id=={}", id);
        publishMessageService.deleteMsgAndTimeline(id);
        return R.ok();
    }

}
