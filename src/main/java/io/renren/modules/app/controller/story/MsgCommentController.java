package io.renren.modules.app.controller.story;

import java.util.List;
import java.util.Map;

import io.renren.common.utils.JsonUtil;
import io.renren.modules.app.dto.CommentDto;
import io.renren.modules.app.form.PageWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.app.entity.story.MsgCommentEntity;
import io.renren.modules.app.service.MsgCommentService;
import io.renren.common.utils.R;



/**
 * 评论
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
@RestController
@RequestMapping("app/comment")
//@Api(tags = "用户评论相关")
public class MsgCommentController {
    private final static Logger logger = LoggerFactory.getLogger(MsgCommentController.class);

    @Autowired
    private MsgCommentService msgCommentService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("获取当前动态资讯下的评论列表")
    public R list(@RequestParam Map<String, Object> params){
        logger.info("[MsgCommentController.list] 请求参数："+params);
        PageWrapper page = new PageWrapper(params);
        if (StringUtils.isEmpty(page.get("pmId"))){
            R.error("pmId must be not null");
        }
        List<CommentDto> list = msgCommentService.getPage(page);
        //级联获取子评论
        recycleSubComment(list);

        return R.ok().put("data", list)
                .put("page",page.getCurrPage())
                .put("size",page.getPageSize());
    }

    /**
     * 信息
     */
    @GetMapping("/info")
    @ApiOperation("获取评论详情")
    public R info(Long id){
        logger.info("[MsgCommentController.info] 请求参数id={}",id);
        MsgCommentEntity msgComment = msgCommentService.selectById(id);
        return R.ok().put("data", msgComment);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存评论")
    public R save(@RequestBody MsgCommentEntity msgComment){
        logger.info("[MsgCommentController.save] 请求参数={}", JsonUtil.Java2Json(msgComment));
        msgComment.setCreateTime(System.currentTimeMillis());
        msgCommentService.insert(msgComment);

        return R.ok();
    }

    /**
     * 修改
     */
    @GetMapping("/update")
    @ApiOperation("更新操作")
    public R update(@RequestBody MsgCommentEntity msgComment){
        logger.info("[MsgCommentController.update] 请求参数={}",msgComment);
        msgCommentService.updateById(msgComment);
        return R.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation("删除")
    public R delete(Long id){
        logger.info("[MsgCommentController.delete] 请求参数id={}",id);
        msgCommentService.deleteById(id);

        return R.ok();
    }

    public void recycleSubComment(List<CommentDto> commentDtoList){
        for(CommentDto commentDto : commentDtoList){
            List<CommentDto> commentDtos = msgCommentService.querySubThroughParentId(commentDto.getPmId(), commentDto.getId());
            if(!CollectionUtils.isEmpty(commentDtos)){
                commentDto.setSubComment(commentDtos);
                recycleSubComment(commentDtos);
            }
        }
    }

}
