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
     * 实付金额
     */
    @Column(name = "real_pay")
    private Integer realPay;

    /**
     * 状态。0：软删除；1：待处理；2：已预约；3：已完成；4：已取消；5：冻结
     */
    private Byte status;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private Date payTime;

    /**
     * 课程总价
     */
    @Column(name = "total_price")
    private Integer totalPrice;

    /**
     * 人次
     */
    @Column(name = "person_time")
    private Byte personTime;

    /**
     * 瘾卡支付金额
     */
    @Column(name = "yen_car_pay_price")
    private Integer yenCarPayPrice;

    /**
     * 瘾卡享受折扣费
     */
    @Column(name = "yen_car_discount")
    private Integer yenCarDiscount;

    /**
     * 瘾卡ID
     */
    @Column(name = "yen_car_id")
    private Long yenCarId;

    /**
     * 在线享受的折扣费
     */
    @Column(name = "online_discount")
    private Integer onlineDiscount;

    /**
     * 在线享受折扣规则，用个表达式
     */
    @Column(name = "online_rule")
    private String onlineRule;

    /**
     * 礼券享受折扣费
     */
    @Column(name = "coupon_discount")
    private Integer couponDiscount;

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
     * 赠送金额
     */
    @Column(name = "gift_discount")
    private Integer giftDiscount;

    /**
     * 类型。1：瘾卡；2：订单
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
     * 获取实付金额
     *
     * @return real_pay - 实付金额
     */
    public Integer getRealPay() {
        return realPay;
    }

    /**
     * 设置实付金额
     *
     * @param realPay 实付金额
     */
    public void setRealPay(Integer realPay) {
        this.realPay = realPay;
    }

    /**
     * 获取状态。0：软删除；1：待处理；2：已预约；3：已完成；4：已取消；5：冻结
     *
     * @return status - 状态。0：软删除；1：待处理；2：已预约；3：已完成；4：已取消；5：冻结
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。0：软删除；1：待处理；2：已预约；3：已完成；4：已取消；5：冻结
     *
     * @param status 状态。0：软删除；1：待处理；2：已预约；3：已完成；4：已取消；5：冻结
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
     * 获取课程总价
     *
     * @return total_price - 课程总价
     */
    public Integer getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置课程总价
     *
     * @param totalPrice 课程总价
     */
    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
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
     * 获取瘾卡支付金额
     *
     * @return yen_car_pay_price - 瘾卡支付金额
     */
    public Integer getYenCarPayPrice() {
        return yenCarPayPrice;
    }

    /**
     * 设置瘾卡支付金额
     *
     * @param yenCarPayPrice 瘾卡支付金额
     */
    public void setYenCarPayPrice(Integer yenCarPayPrice) {
        this.yenCarPayPrice = yenCarPayPrice;
    }

    /**
     * 获取瘾卡享受折扣费
     *
     * @return yen_car_discount - 瘾卡享受折扣费
     */
    public Integer getYenCarDiscount() {
        return yenCarDiscount;
    }

    /**
     * 设置瘾卡享受折扣费
     *
     * @param yenCarDiscount 瘾卡享受折扣费
     */
    public void setYenCarDiscount(Integer yenCarDiscount) {
        this.yenCarDiscount = yenCarDiscount;
    }

    /**
     * 获取瘾卡ID
     *
     * @return yen_car_id - 瘾卡ID
     */
    public Long getYenCarId() {
        return yenCarId;
    }

    /**
     * 设置瘾卡ID
     *
     * @param yenCarId 瘾卡ID
     */
    public void setYenCarId(Long yenCarId) {
        this.yenCarId = yenCarId;
    }

    /**
     * 获取在线享受的折扣费
     *
     * @return online_discount - 在线享受的折扣费
     */
    public Integer getOnlineDiscount() {
        return onlineDiscount;
    }

    /**
     * 设置在线享受的折扣费
     *
     * @param onlineDiscount 在线享受的折扣费
     */
    public void setOnlineDiscount(Integer onlineDiscount) {
        this.onlineDiscount = onlineDiscount;
    }

    /**
     * 获取在线享受折扣规则，用个表达式
     *
     * @return online_rule - 在线享受折扣规则，用个表达式
     */
    public String getOnlineRule() {
        return onlineRule;
    }

    /**
     * 设置在线享受折扣规则，用个表达式
     *
     * @param onlineRule 在线享受折扣规则，用个表达式
     */
    public void setOnlineRule(String onlineRule) {
        this.onlineRule = onlineRule;
    }

    /**
     * 获取礼券享受折扣费
     *
     * @return coupon_discount - 礼券享受折扣费
     */
    public Integer getCouponDiscount() {
        return couponDiscount;
    }

    /**
     * 设置礼券享受折扣费
     *
     * @param couponDiscount 礼券享受折扣费
     */
    public void setCouponDiscount(Integer couponDiscount) {
        this.couponDiscount = couponDiscount;
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
     * 获取赠送金额
     *
     * @return gift_discount - 赠送金额
     */
    public Integer getGiftDiscount() {
        return giftDiscount;
    }

    /**
     * 设置赠送金额
     *
     * @param giftDiscount 赠送金额
     */
    public void setGiftDiscount(Integer giftDiscount) {
        this.giftDiscount = giftDiscount;
    }

    /**
     * 获取类型。1：瘾卡；2：订单
     *
     * @return type - 类型。1：瘾卡；2：订单
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型。1：瘾卡；2：订单
     *
     * @param type 类型。1：瘾卡；2：订单
     */
    public void setType(Byte type) {
        this.type = type;
    }
}