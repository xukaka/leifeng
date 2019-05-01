package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.entity.task.TaskCircleEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskCircleForm;

import java.util.List;

/**
 * 首页
 */
public interface HomeService  {

    /**
     * 获取首页横幅列表
     */
    List<String> getBanners();


}

