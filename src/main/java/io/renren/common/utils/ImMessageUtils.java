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
import io.renren.modules.app.dto.MemberDto;
import org.jim.common.packets.ChatBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * IM 消息工具类
 */
public final class ImMessageUtils{
    private final static Logger logger = LoggerFactory.getLogger(ImMessageUtils.class);
    private final static String IM_MESSAGE_URL = "https://pet.fangzheng.fun:11805/api/message/send";

    //圈申请加入消息
    public static String getCircleJoinMessage(String circleId,String auditId,String content,String businessCode,String toId,String fromId){
        JSONObject msg = new JSONObject();
        JSONObject extras = new JSONObject();
        extras.put("businessCode", businessCode);
        extras.put("circleId", circleId);
        extras.put("auditId", auditId);
        extras.put("content", content);
        msg.put("toId", toId);
        msg.put("fromId", fromId);
        msg.put("extras",extras);
        return msg.toJSONString();
    }

    //圈审核结果消息
    public static String getCircleAuditMessage(String auditId,String content,String businessCode,String toId,String fromId){
        JSONObject msg = new JSONObject();
        JSONObject extras = new JSONObject();
        extras.put("businessCode", businessCode);
        extras.put("auditId", auditId);
        extras.put("content", content);
        msg.put("toId", toId);
        msg.put("fromId", fromId);
        msg.put("extras",extras);
        return msg.toJSONString();
    }

    //任务状态消息
    public static String getTaskStatusMessage(Long taskId,String content,String to,String from){
        JSONObject msg = new JSONObject();
        msg.put("taskId", taskId);
        msg.put("content", content);
        msg.put("to", to);
        msg.put("from", from);
        return msg.toJSONString();
    }

    //群组消息
    public static String getGroupMsg(String groupId, String businessType, Long businessId, String content, String from) {
        JSONObject groupMsg = new JSONObject();
        groupMsg.put("businessType", businessType);//类型为任务|笔记
        groupMsg.put("businessId", businessId);//任务/笔记id
        groupMsg.put("content", content);
        groupMsg.put("from", from);
        groupMsg.put("groupId", groupId);
        return groupMsg.toJSONString();
    }


    //推送单个消息
    public static void sendSingleMessage(JSONObject object){
        ChatBody chatBody= new ChatBody.Builder()
                .setFrom(object.getString("fromId"))
                .setTo(object.getString("toId"))
                .setChatType(2)
                .setMsgType(0)
                .setCmd(11)
                .setContent("任务状态变更通知")
                .setCreateTime(DateUtils.now())
                .addExtra("data",object.get("extras"))
                .build();
        String params = JSONObject.toJSONString(chatBody);
        try {
            String result =   HttpHelper.post(null,params,IM_MESSAGE_URL,3000);
            logger.info(result);
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
    }
    //推送单个消息
    public static void sendSingleMessage(String from ,String content, String to, Object extras){
        ChatBody chatBody= new ChatBody.Builder()
                .setFrom(from)
                .setTo(to)
                .setChatType(2)
                .setMsgType(0)
                .setCmd(11)
                .setContent(content)
                .setCreateTime(DateUtils.now())
                .addExtra("data",extras)
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
    public static void sendGroupMessage(String from , String groupId){
        ChatBody chatBody= new ChatBody.Builder()
                .setFrom(from)
                .setChatType(1)
                .setMsgType(0)
                .setGroup_id(groupId)
                .setCmd(11)
                .setCreateTime(DateUtils.now())
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
