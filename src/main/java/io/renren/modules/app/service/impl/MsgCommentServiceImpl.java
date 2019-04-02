package io.renren.modules.app.service.impl;

import io.renren.modules.app.dto.CommentDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.app.dao.story.MsgCommentDao;
import io.renren.modules.app.entity.story.MsgCommentEntity;
import io.renren.modules.app.service.MsgCommentService;


@Service("msgCommentService")
public class MsgCommentServiceImpl extends ServiceImpl<MsgCommentDao, MsgCommentEntity> implements MsgCommentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MsgCommentEntity> page = this.selectPage(
                new Query<MsgCommentEntity>(params).getPage(),
                new EntityWrapper<MsgCommentEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CommentDto> getPage(HashMap params) {
        return this.baseMapper.getPage(params);
    }

    @Override
    public List<CommentDto> querySubThroughParentId(Long fsmId, Long parentId) {
        return this.baseMapper.querySubThroughParentId(fsmId,parentId);
    }

}
