package io.renren.modules.app.controller.story;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.PageWrapperUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.CommentDto;
import io.renren.modules.app.entity.CommentTypeEnum;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.CommentService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/app/comment/diary")
@Api(tags = "日记评论")
public class DiaryCommentController {

    @Autowired
    private CommentService commentService;

    @Login
    @PostMapping("/add")
    @ApiOperation("新增评论")
    public R addComment( Long diaryId,  String content) {
        commentService.addComment(diaryId,CommentTypeEnum.diary, ReqUtils.curMemberId(), content);
        return R.ok();
    }

    @GetMapping("/list")
    @ApiOperation("分页获取评论列表")
    public R getComments(Long diaryId, Integer curPage,  Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<CommentDto> comments = commentService.getComments(diaryId,CommentTypeEnum.diary, page);
        return R.ok().put("result", comments);
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除评论")
    public R deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return R.ok();
    }


    @Login
    @PostMapping("/reply/add")
    @ApiOperation("新增评论回复")
    public R addCommentReply( Long commentId,  Long toUserId,  String content) {
        commentService.addCommentReply(commentId,ReqUtils.curMemberId(),toUserId,content);
        return R.ok();
    }


    @DeleteMapping("/reply/delete/{id}")
    @ApiOperation("删除评论回复")
    public R deleteCommentReply(@PathVariable Long id) {
        commentService.deleteCommentReply(id);
        return R.ok();
    }

}
