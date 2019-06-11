package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.form.DiaryForm;
import io.renren.modules.app.form.PageWrapper;

public interface DiaryService extends IService<DiaryEntity> {

    /**
     * 创建日记
     */
     void createDiary(Long memberId,DiaryForm form);

    /**
     * 获取日记详情
     */
    DiaryDto getDiary(Long curMemberId,Long id);

    /**
     * 分页获取日记列表
     */
    PageUtils<DiaryDto> getDiarys(PageWrapper page);

    /**
     * 分页获取我的日记列表
     */
    PageUtils<DiaryDto> getMyDiarys(Long creatorId,PageWrapper page);
    /**
     * 日记点赞数+inc
     */
    void incLikeCount(Long diaryId, Integer inc);
    /**
     * 日记评论数+inc
     */
     void incCommentCount(Long diaryId, Integer inc);
    /**
     * 日记浏览数+inc
     */
     void incViewCount(Long diaryId, Integer inc) ;
}
