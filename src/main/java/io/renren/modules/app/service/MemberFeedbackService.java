package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.setting.MemberFeedback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 16:03:12
 */
public interface MemberFeedbackService extends IService<MemberFeedback> {

    PageUtils queryPage(Map<String, Object> params);

    List<MemberFeedback> getPage(HashMap<String,Object> param);
}

