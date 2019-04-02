package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.setting.MemberFriend;
import io.renren.modules.app.entity.setting.MemberFriend;

import java.util.Map;

/**
 * 
 *
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 16:03:12
 */
public interface MemberFriendService extends IService<MemberFriend> {

    PageUtils queryPage(Map<String, Object> params);
}

