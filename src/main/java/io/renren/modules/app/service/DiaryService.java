package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.form.DiaryForm;

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
}
