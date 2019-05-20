package io.renren.modules.app.dto;

import io.renren.modules.app.entity.TaskStatusEnum;

import java.io.Serializable;

public class TaskOrderDto implements Serializable {

    private Long id;
    //微信支付订单号
    private String transactionId;
    //商户订单号
    private String outTradeNo;
    //关联任务id
    private Long taskId;
    //任务标题
    private String taskTitle;
    //任务状态
    private TaskStatusEnum taskStatus;
    //任务创建人昵称
    private String taskCreatorNickName;
    //任务创建人id
    private Long taskCreatorId;
    //订单金额，单位为分
    private Long totalFee;
    //交易状态，SUCCESS—支付成功,REFUND—转入退款,NOTPAY—未支付
    // CLOSED—已关闭,REVOKED—已撤销（刷卡支付）,USERPAYING--用户支付中,PAYERROR--支付失败
    private String tradeState;
    //附件描述信息
    private String attach;
    //支付时间格式为yyyyMMddHHmmss
    private String timeEnd;

    //创建时间
    private Long createTime;

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public TaskStatusEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusEnum taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskCreatorNickName() {
        return taskCreatorNickName;
    }

    public void setTaskCreatorNickName(String taskCreatorNickName) {
        this.taskCreatorNickName = taskCreatorNickName;
    }

    public Long getTaskCreatorId() {
        return taskCreatorId;
    }

    public void setTaskCreatorId(Long taskCreatorId) {
        this.taskCreatorId = taskCreatorId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
