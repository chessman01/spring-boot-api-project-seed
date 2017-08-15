package com.tianbao.buy.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "fund_detail")
public class FundDetail {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 支付通道。0：瘾卡；1：微信
     */
    private Byte channel;

    /**
     * 支付目标。0：充值瘾卡；1：按次消费
     */
    private Byte target;

    /**
     * 原始ID。当target为0时瘾卡ID；target为1时内部订单ID。
     */
    @Column(name = "orgin_id")
    private Long orginId;

    /**
     * 支付金额(单位：分)
     */
    private Integer price;

    /**
     * 外部支付订单号。微信、支付宝支付时才有
     */
    @Column(name = "pay_order_id")
    private String payOrderId;

    /**
     * 流动方向。0：支出；1：退款
     */
    private Byte direction;

    /**
     * 充值赠送使用礼券Id
     */
    @Column(name = "recharge_coupone_id")
    private Integer rechargeCouponeId;

    /**
     * 账号类型。1：现金；2：赠送
     */
    @Column(name = "account_type")
    private Byte accountType;

    /**
     * 状态。0：软删除；1：待处理；2：已处理；3：冻结
     */
    private Byte status;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取支付通道。0：瘾卡；1：微信
     *
     * @return channel - 支付通道。0：瘾卡；1：微信
     */
    public Byte getChannel() {
        return channel;
    }

    /**
     * 设置支付通道。0：瘾卡；1：微信
     *
     * @param channel 支付通道。0：瘾卡；1：微信
     */
    public void setChannel(Byte channel) {
        this.channel = channel;
    }

    /**
     * 获取支付目标。0：充值瘾卡；1：按次消费
     *
     * @return target - 支付目标。0：充值瘾卡；1：按次消费
     */
    public Byte getTarget() {
        return target;
    }

    /**
     * 设置支付目标。0：充值瘾卡；1：按次消费
     *
     * @param target 支付目标。0：充值瘾卡；1：按次消费
     */
    public void setTarget(Byte target) {
        this.target = target;
    }

    /**
     * 获取原始ID。当target为0时瘾卡ID；target为1时内部订单ID。
     *
     * @return orgin_id - 原始ID。当target为0时瘾卡ID；target为1时内部订单ID。
     */
    public Long getOrginId() {
        return orginId;
    }

    /**
     * 设置原始ID。当target为0时瘾卡ID；target为1时内部订单ID。
     *
     * @param orginId 原始ID。当target为0时瘾卡ID；target为1时内部订单ID。
     */
    public void setOrginId(Long orginId) {
        this.orginId = orginId;
    }

    /**
     * 获取支付金额(单位：分)
     *
     * @return price - 支付金额(单位：分)
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置支付金额(单位：分)
     *
     * @param price 支付金额(单位：分)
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * 获取外部支付订单号。微信、支付宝支付时才有
     *
     * @return pay_order_id - 外部支付订单号。微信、支付宝支付时才有
     */
    public String getPayOrderId() {
        return payOrderId;
    }

    /**
     * 设置外部支付订单号。微信、支付宝支付时才有
     *
     * @param payOrderId 外部支付订单号。微信、支付宝支付时才有
     */
    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    /**
     * 获取流动方向。0：支出；1：退款
     *
     * @return direction - 流动方向。0：支出；1：退款
     */
    public Byte getDirection() {
        return direction;
    }

    /**
     * 设置流动方向。0：支出；1：退款
     *
     * @param direction 流动方向。0：支出；1：退款
     */
    public void setDirection(Byte direction) {
        this.direction = direction;
    }

    /**
     * 获取充值赠送使用礼券Id
     *
     * @return recharge_coupone_id - 充值赠送使用礼券Id
     */
    public Integer getRechargeCouponeId() {
        return rechargeCouponeId;
    }

    /**
     * 设置充值赠送使用礼券Id
     *
     * @param rechargeCouponeId 充值赠送使用礼券Id
     */
    public void setRechargeCouponeId(Integer rechargeCouponeId) {
        this.rechargeCouponeId = rechargeCouponeId;
    }

    /**
     * 获取账号类型。1：现金；2：赠送
     *
     * @return account_type - 账号类型。1：现金；2：赠送
     */
    public Byte getAccountType() {
        return accountType;
    }

    /**
     * 设置账号类型。1：现金；2：赠送
     *
     * @param accountType 账号类型。1：现金；2：赠送
     */
    public void setAccountType(Byte accountType) {
        this.accountType = accountType;
    }

    /**
     * 获取状态。0：软删除；1：待处理；2：已处理；3：冻结
     *
     * @return status - 状态。0：软删除；1：待处理；2：已处理；3：冻结
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。0：软删除；1：待处理；2：已处理；3：冻结
     *
     * @param status 状态。0：软删除；1：待处理；2：已处理；3：冻结
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}