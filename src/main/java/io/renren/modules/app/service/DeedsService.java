package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.app.dto.DeedsDto;
import io.renren.modules.app.entity.story.DeedsEntity;
import io.renren.modules.app.form.DeedsForm;

import java.util.List;

/**
 * @author huangshishui
 * @date 2019/4/18 23:57
 **/
public interface DeedsService extends IService<DeedsEntity> {


    void insertDeedsInfo(DeedsForm deedsForm) throws Exception;

    void updateReadCount(String deedsId) throws Exception;

    List<DeedsDto> queryDeedsList(String userId) throws Exception;





}
