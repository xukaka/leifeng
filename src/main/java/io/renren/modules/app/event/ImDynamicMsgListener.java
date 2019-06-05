package io.renren.modules.app.event;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.dao.member.MemberFollowDao;
import io.renren.modules.app.entity.member.MemberFollowEntity;
import io.renren.modules.app.service.ImService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@RabbitListener(queues = RabbitMQConfig.IM_QUEUE_DYNAMIC)
public class ImDynamicMsgListener {
    private static Logger LOG = LoggerFactory.getLogger(ImDynamicMsgListener.class);

    @Autowired
    private ImService imService;

    @Autowired
    private MemberFollowDao memberFollowDao;


    @RabbitHandler
    public void handleMessage(String message) {
        LOG.info("rabbitMQ handle dynamic message ===>>> " + message);
        JSONObject dynamicMsg = JSONObject.parseObject(message);
        Long memberId = dynamicMsg.getLong("memberId");
        String businessType = dynamicMsg.getString("businessType");
        Long businessId = dynamicMsg.getLong("businessId");
        imService.addDynamicNotice(memberId, businessType, businessId);

        //设置红点
        setRedDot(memberId);
    }

    //所有关注我的用户收到消息
    private void setRedDot(Long memberId) {
        Wrapper<MemberFollowEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("to_member_id", memberId);
        List<MemberFollowEntity> fans = memberFollowDao.selectList(wrapper);
        if (!CollectionUtils.isEmpty(fans)){
            for (MemberFollowEntity fan: fans){
                Long fanId = fan.getFromMemberId();
                imService.setRedDot(fanId,2);
            }
        }
    }
}

