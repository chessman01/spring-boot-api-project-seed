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
     * 订单ID
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 支付金额(单位：分)
     */
    private Integer price;

    /**
     * 支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     */
    private Byte from;

    /**
     * 支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     */
    private Byte to;

    /**
     * 流动方向。1：瘾卡充值退回；2：瘾卡充值进账；3：按次消费退回；4：按次消费进账；
     */
    private Byte direction;

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
     * 获取订单ID
     *
     * @return order_id - 订单ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单ID
     *
     * @param orderId 订单ID
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
     * 获取支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     *
     * @return from - 支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     */
    public Byte getFrom() {
        return from;
    }

    /**
     * 设置支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     *
     * @param from 支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     */
    public void setFrom(Byte from) {
        this.from = from;
    }

    /**
     * 获取支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     *
     * @return to - 支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     */
    public Byte getTo() {
        return to;
    }

    /**
     * 设置支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     *
     * @param to 支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
     */
    public void setTo(Byte to) {
        this.to = to;
    }

    /**
     * 获取流动方向。1：瘾卡充值退回；2：瘾卡充值进账；3：按次消费退回；4：按次消费进账；
     *
     * @return direction - 流动方向。1：瘾卡充值退回；2：瘾卡充值进账；3：按次消费退回；4：按次消费进账；
     */
    public Byte getDirection() {
        return direction;
    }

    /**
     * 设置流动方向。1：瘾卡充值退回；2：瘾卡充值进账；3：按次消费退回；4：按次消费进账；
     *
     * @param direction 流动方向。1：瘾卡充值退回；2：瘾卡充值进账；3：按次消费退回；4：按次消费进账；
     */
    public void setDirection(Byte direction) {
        this.direction = direction;
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