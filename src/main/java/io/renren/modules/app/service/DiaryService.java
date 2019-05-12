package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.form.DiaryForm;
import io.renren.modules.app.form.PageWrapper;

/**
 * @author huangshishui
 * @date 2019/4/18 23:57
 **/
public interface DiaryService extends IService<DiaryEntity> {

    /**
     * 创建日记
     * @param memberId
     * @param form
     */
     void createDiary(Long memberId,DiaryForm form);

    /**
     * 获取日记详情
     * @param id
     * @return
     */
    DiaryDto getDiary(Long id);

    /**
     * 分页获取日记列表
     * @param page
     * @return
     */
    PageUtils<DiaryDto> getDiarys(PageWrapper page);

    /**
     * 分页获取我的日记列表
     * @param page
     * @return
     */
    PageUtils<DiaryDto> getMyDiarys(Long creatorId,PageWrapper page);
}
