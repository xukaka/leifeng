package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.ThreadPoolUtils;
import io.renren.modules.app.dao.task.CommentDao;
import io.renren.modules.app.dao.task.CommentReplyDao;
import io.renren.modules.app.dao.task.LikeDao;
import io.renren.modules.app.dto.CommentDto;
import io.renren.modules.app.dto.CommentReplyDto;
import io.renren.modules.app.entity.CommentTypeEnum;
import io.renren.modules.app.entity.LikeTypeEnum;
import io.renren.modules.app.entity.task.CommentEntity;
import io.renren.modules.app.entity.task.CommentReplyEntity;
import io.renren.modules.app.entity.task.LikeEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.CommentService;
import io.renren.modules.app.service.DiaryService;
import io.renren.modules.app.service.LikeService;
import io.renren.modules.app.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeDao, LikeEntity> implements LikeService {
    private final static Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);
    @Resource
    private LikeDao likeDao;

    @Resource
    private TaskService taskService;
    @Resource
    private DiaryService diaryService;

    @Override
    public void like(Long memberId, Long businessId, LikeTypeEnum type) {
        LikeEntity like = new LikeEntity(DateUtils.now(), businessId, type, memberId);
        likeDao.insert(like);
        ThreadPoolUtils.execute(() -> {
            //点赞数+1
            switch (type){
                case task:
                    taskService.incLikeCount(businessId, 1);
                    break;
                case diary:
                    diaryService.incLikeCount(businessId, 1);
                    break;
            }

        });
    }

    @Override
    public void unlike(Long memberId, Long businessId, LikeTypeEnum type) {
        Wrapper<LikeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("business_id", businessId)
                .eq("type", type)
                .eq("member_id", memberId);
        likeDao.delete(wrapper);

        ThreadPoolUtils.execute(() -> {
            //点赞数-1
            switch (type){
                case task:
                    taskService.incLikeCount(businessId, -1);
                    break;
                case diary:
                    diaryService.incLikeCount(businessId, -1);
                    break;
            }
        });
    }

    /**
     * 是否点赞
     */
    public boolean existsLiked(Long businessId, LikeTypeEnum type, Long memberId) {
        Wrapper<LikeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("business_id", businessId)
                .eq("type", type)
                .eq("member_id", memberId);
        int count = likeDao.selectCount(wrapper);
        return count > 0;
    }

}