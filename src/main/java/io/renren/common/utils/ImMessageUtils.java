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
    public static String getTaskStatusMessage(String taskId,String content,String businessCode,String toId,String fromId){
        JSONObject msg = new JSONObject();
        JSONObject extras = new JSONObject();
        extras.put("businessCode", businessCode);
        extras.put("taskId", taskId);
        extras.put("content", content);
        msg.put("toId", toId);
        msg.put("fromId", fromId);
        msg.put("extras",extras);
        return msg.toJSONString();
    }

//    组消息
    public static String getGroupMessage(String id,String type,String content,String businessCode,String groupId,String from){
        JSONObject msg = new JSONObject();
        JSONObject extras = new JSONObject();
        extras.put("businessCode", businessCode);//10，发布任务/笔记
        extras.put("type", type);//类型为任务|笔记
        extras.put("id", id);
        extras.put("content", content);
        msg.put("from", from);
        msg.put("groupId", groupId);
        msg.put("extras",extras);
        return msg.toJSONString();
    }

    public  static void sendTaskStatusMessage(String taskId,String content,String businessCode,String toId,String fromId){
        JSONObject msg = new JSONObject();
        JSONObject extras = new JSONObject();
        extras.put("businessCode", businessCode);//2，确认领取任务
        extras.put("taskId", taskId);
        extras.put("content", content);
        msg.put("toId", toId);
        msg.put("fromId", fromId);
        msg.put("extras",extras);
        logger.info("任务状态变更消息，msg=" + msg.toJSONString());
        sendSingleMessage(msg);
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
    public static void sendSingleMessage(String from ,String content, String to, JSONObject object){

        ChatBody chatBody= new ChatBody.Builder()
                .setFrom(from)
                .setTo(to)
                .setChatType(2)
                .setMsgType(0)
                .setCmd(11)
                .setContent(content)
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
    //推送群组消息
    public static void sendGroupMessage(JSONObject groupMsg){

        ChatBody chatBody= new ChatBody.Builder()
                .setFrom(groupMsg.getString("from"))
                .setChatType(1)
                .setMsgType(0)
                .setGroup_id(groupMsg.getString("groupId"))
                .setCmd(11)
                .setCreateTime(DateUtils.now())
                .addExtra("data",groupMsg.get("extras"))
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
