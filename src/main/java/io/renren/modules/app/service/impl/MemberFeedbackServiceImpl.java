package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.app.dao.member.MemberFeedbackDao;
import io.renren.modules.app.entity.member.MemberFeedback;
import io.renren.modules.app.service.MemberFeedbackService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("memberFeedbackService")
public class MemberFeedbackServiceImpl extends ServiceImpl<MemberFeedbackDao, MemberFeedback> implements MemberFeedbackService {

    @Override
    public PageUtils<MemberFeedback> queryPage(Map<String, Object> params) {
        Page<MemberFeedback> page = this.selectPage(
                new Query<MemberFeedback>(params).getPage(),
                new EntityWrapper<>()
        );
        return new PageUtils<>(page);
    }

    @Override
    public List<MemberFeedback> getPage(HashMap<String, Object> param) {
        return baseMapper.getPage(param);
    }

}
