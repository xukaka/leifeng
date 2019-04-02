package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.CommentDto;
import io.renren.modules.app.entity.story.MsgCommentEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
public interface MsgCommentService extends IService<MsgCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CommentDto> getPage(HashMap params);

    List<CommentDto> querySubThroughParentId(Long fsmId , Long parentId);
}

