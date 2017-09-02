package com.tianbao.buy.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "order_main")
public class OrderMain {
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
     * 订单号
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 课程ID
     */
    @Column(name = "class_id")
    private Long classId;

    /**
     * 状态。0：软删除；1：待支付；2：已预约；3：已完成；4：待取消；5：已取消；6：冻结
     */
    private Byte status;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private Date payTime;

    /**
     * 人次
     */
    @Column(name = "person_time")
    private Byte personTime;

    /**
     * 瘾卡ID
     */
    @Column(name = "yen_card_id")
    private Long yenCardId;

    /**
     * 礼券ID
     */
    @Column(name = "coupon_id")
    private Long couponId;

    /**
     * 外部支付订单号
     */
    @Column(name = "pay_order_id")
    private String payOrderId;

    /**
     * 类型。1：瘾卡；2：课程
     */
    private Byte type;

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
     * 获取订单号
     *
     * @return order_id - 订单号
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单号
     *
     * @param orderId 订单号
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
     * 获取课程ID
     *
     * @return class_id - 课程ID
     */
    public Long getClassId() {
        return classId;
    }

    /**
     * 设置课程ID
     *
     * @param classId 课程ID
     */
    public void setClassId(Long classId) {
        this.classId = classId;
    }

    /**
     * 获取状态。0：软删除；1：待支付；2：已预约；3：已完成；4：待取消；5：已取消；6：冻结
     *
     * @return status - 状态。0：软删除；1：待支付；2：已预约；3：已完成；4：待取消；5：已取消；6：冻结
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。0：软删除；1：待支付；2：已预约；3：已完成；4：待取消；5：已取消；6：冻结
     *
     * @param status 状态。0：软删除；1：待支付；2：已预约；3：已完成；4：待取消；5：已取消；6：冻结
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取支付时间
     *
     * @return pay_time - 支付时间
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 设置支付时间
     *
     * @param payTime 支付时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取人次
     *
     * @return person_time - 人次
     */
    public Byte getPersonTime() {
        return personTime;
    }

    /**
     * 设置人次
     *
     * @param personTime 人次
     */
    public void setPersonTime(Byte personTime) {
        this.personTime = personTime;
    }

    /**
     * 获取瘾卡ID
     *
     * @return yen_card_id - 瘾卡ID
     */
    public Long getYenCardId() {
        return yenCardId;
    }

    /**
     * 设置瘾卡ID
     *
     * @param yenCardId 瘾卡ID
     */
    public void setYenCardId(Long yenCardId) {
        this.yenCardId = yenCardId;
    }

    /**
     * 获取礼券ID
     *
     * @return coupon_id - 礼券ID
     */
    public Long getCouponId() {
        return couponId;
    }

    /**
     * 设置礼券ID
     *
     * @param couponId 礼券ID
     */
    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    /**
     * 获取外部支付订单号
     *
     * @return pay_order_id - 外部支付订单号
     */
    public String getPayOrderId() {
        return payOrderId;
    }

    /**
     * 设置外部支付订单号
     *
     * @param payOrderId 外部支付订单号
     */
    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    /**
     * 获取类型。1：瘾卡；2：课程
     *
     * @return type - 类型。1：瘾卡；2：课程
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型。1：瘾卡；2：课程
     *
     * @param type 类型。1：瘾卡；2：课程
     */
    public void setType(Byte type) {
        this.type = type;
    }
}