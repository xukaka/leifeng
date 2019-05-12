package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.entity.LikeTypeEnum;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.entity.task.LikeEntity;
import io.renren.modules.app.form.DiaryForm;
import io.renren.modules.app.form.PageWrapper;


public interface LikeService extends IService<LikeEntity> {

    /**
     * 点赞
     */
    void like(Long memberId, Long businessId, LikeTypeEnum type);

    /**
     * 取消点赞
     */
    void unlike(Long memberId, Long businessId,LikeTypeEnum type);

    /**
     * 是否点赞
     */
    boolean existsLiked(Long businessId, LikeTypeEnum type,Long memberId);


}
