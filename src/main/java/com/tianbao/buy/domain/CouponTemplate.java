package com.tianbao.buy.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "coupon_template")
public class CouponTemplate {
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
     * 更新时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 面值。单位（元）
     */
    private Integer price;

    /**
     * 来源类型。1：邀请好友；2：线下领取；3：微信领取；
     */
    private Byte source;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 规则。1：满送；2:抵扣。注意：此字段暂不用
     */
    private Byte rule;

    /**
     * 条件值。单位：分
     */
    @Column(name = "rule_price")
    private Integer rulePrice;

    /**
     * 支付类型。1：瘾卡充值；2：按次支付；
     */
    @Column(name = "pay_type")
    private Byte payType;

    /**
     * 状态。0：删除；1：正常；2：已过期；8：充值瘾卡赠送模版-保留；
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
     * 获取更新时间
     *
     * @return modify_time - 更新时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置更新时间
     *
     * @param modifyTime 更新时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取面值。单位（元）
     *
     * @return price - 面值。单位（元）
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置面值。单位（元）
     *
     * @param price 面值。单位（元）
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * 获取来源类型。1：邀请好友；2：线下领取；3：微信领取；
     *
     * @return source - 来源类型。1：邀请好友；2：线下领取；3：微信领取；
     */
    public Byte getSource() {
        return source;
    }

    /**
     * 设置来源类型。1：邀请好友；2：线下领取；3：微信领取；
     *
     * @param source 来源类型。1：邀请好友；2：线下领取；3：微信领取；
     */
    public void setSource(Byte source) {
        this.source = source;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取规则。1：满送；2:抵扣。注意：此字段暂不用
     *
     * @return rule - 规则。1：满送；2:抵扣。注意：此字段暂不用
     */
    public Byte getRule() {
        return rule;
    }

    /**
     * 设置规则。1：满送；2:抵扣。注意：此字段暂不用
     *
     * @param rule 规则。1：满送；2:抵扣。注意：此字段暂不用
     */
    public void setRule(Byte rule) {
        this.rule = rule;
    }

    /**
     * 获取条件值。单位：分
     *
     * @return rule_price - 条件值。单位：分
     */
    public Integer getRulePrice() {
        return rulePrice;
    }

    /**
     * 设置条件值。单位：分
     *
     * @param rulePrice 条件值。单位：分
     */
    public void setRulePrice(Integer rulePrice) {
        this.rulePrice = rulePrice;
    }

    /**
     * 获取支付类型。1：瘾卡充值；2：按次支付；
     *
     * @return pay_type - 支付类型。1：瘾卡充值；2：按次支付；
     */
    public Byte getPayType() {
        return payType;
    }

    /**
     * 设置支付类型。1：瘾卡充值；2：按次支付；
     *
     * @param payType 支付类型。1：瘾卡充值；2：按次支付；
     */
    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    /**
     * 获取状态。0：删除；1：正常；2：已过期；8：充值瘾卡赠送模版-保留；
     *
     * @return status - 状态。0：删除；1：正常；2：已过期；8：充值瘾卡赠送模版-保留；
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。0：删除；1：正常；2：已过期；8：充值瘾卡赠送模版-保留；
     *
     * @param status 状态。0：删除；1：正常；2：已过期；8：充值瘾卡赠送模版-保留；
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}