package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.app.dao.setting.MemberFeedbackDao;
import io.renren.modules.app.dao.setting.MemberFriendDao;
import io.renren.modules.app.entity.setting.MemberFeedback;
import io.renren.modules.app.entity.setting.MemberFriend;
import io.renren.modules.app.service.MemberFeedbackService;
import io.renren.modules.app.service.MemberFriendService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("memberFeedbackService")
public class MemberFeedbackServiceImpl extends ServiceImpl<MemberFeedbackDao, MemberFeedback> implements MemberFeedbackService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MemberFeedback> page = this.selectPage(
                new Query<MemberFeedback>(params).getPage(),
                new EntityWrapper<MemberFeedback>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<MemberFeedback> getPage(HashMap<String, Object> param) {
        return this.baseMapper.getPage(param);
    }

}
