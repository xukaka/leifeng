/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.common.utils;


import com.alibaba.fastjson.JSONObject;
import io.renren.common.io.SocketServiceUtil;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.service.MemberService;
import org.jim.common.packets.ChatBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;


/**
 * IM 消息工具类
 */
public final class ImMessageUtils{
    private final static Logger logger = LoggerFactory.getLogger(ImMessageUtils.class);
    private final static String IM_MESSAGE_URL = "https://pet.fangzheng.fun:11805/api/message/send";
    @Resource
    private static RabbitMqHelper rabbitMqHelper;
    public  static void sendTaskStatusMessage(String taskId,String conTent,String businessCode,String toId,String formId){
        RabbitMqHelper rabbitMqHelper = SocketServiceUtil.getBean(RabbitMqHelper.class);
        JSONObject object = new JSONObject();
        JSONObject extras = new JSONObject();
        extras.put("businessCode", businessCode);//2，确认领取任务
        extras.put("taskId", taskId);
        extras.put("conTent", conTent);
        object.put("toId", toId);
        object.put("formId", formId);
        object.put("extras",extras);
        logger.info("推送消息给任务接收人，extras=" + extras.toJSONString());
        rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_NAME, extras.toJSONString());
    }
    //推送单个消息
    public static void sendSingleMessage(String from ,String content, String to, JSONObject object){

        ChatBody chatBody= new ChatBody.Builder()
                .setFrom(from)
                .setTo(to)
                .setChatType(2)
                .setMsgType(0)
                .setCmd(11)
                .setContent(content)
                .setCreateTime(DateUtils.now())
                .addExtra("imMsg",object.get("extras"))
                .build();
        String params = JSONObject.toJSONString(chatBody);
        try {
            String result =   HttpHelper.post(null,params,IM_MESSAGE_URL,3000);
            logger.info(result);
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
    }

    //推送群组消息
    public static void sendGroupMessage(String from , String groupId, JSONObject extras){

        ChatBody chatBody= new ChatBody.Builder()
                .setFrom(from)
                .setChatType(1)
                .setMsgType(0)
                .setGroup_id(groupId)
                .setCmd(11)
                .setCreateTime(DateUtils.now())
                .addExtra("imMsg",extras)
                .build();
        String params = JSONObject.toJSONString(chatBody);
        try {
            String result =   HttpHelper.post(null,params,IM_MESSAGE_URL,3000);
            logger.info(result);
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
    }
}
