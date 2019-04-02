package io.renren.modules.app.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.app.dao.setting.MemberFriendDao;
import io.renren.modules.app.entity.setting.MemberFriend;
import io.renren.modules.app.service.MemberFriendService;


@Service("memberFriendService")
public class MemberFriendServiceImpl extends ServiceImpl<MemberFriendDao, MemberFriend> implements MemberFriendService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MemberFriend> page = this.selectPage(
                new Query<MemberFriend>(params).getPage(),
                new EntityWrapper<MemberFriend>()
        );

        return new PageUtils(page);
    }

}
