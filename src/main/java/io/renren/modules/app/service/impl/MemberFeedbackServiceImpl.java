package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.app.dao.member.MemberFeedbackDao;
import io.renren.modules.app.entity.member.MemberFeedback;
import io.renren.modules.app.service.MemberFeedbackService;
import org.springframework.stereotype.Service;


@Service
public class MemberFeedbackServiceImpl extends ServiceImpl<MemberFeedbackDao, MemberFeedback> implements MemberFeedbackService {

}
