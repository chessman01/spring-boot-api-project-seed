package com.tianbao.buy.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "yen_care")
public class YenCare {
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
     * 现金账号（单位：分）
     */
    @Column(name = "cash_account")
    private Integer cashAccount;

    /**
     * 赠送账号（单位：分）
     */
    @Column(name = "gift_account")
    private Integer giftAccount;

    /**
     * 折扣率（实际使用要除100）
     */
    @Column(name = "discount_rate")
    private Byte discountRate;

    /**
     * 类型。1：普通；2：情侣
     */
    private Byte type;

    /**
     * 状态。0：软删除；1:正常；2：冻结
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
     * 获取现金账号（单位：分）
     *
     * @return cash_account - 现金账号（单位：分）
     */
    public Integer getCashAccount() {
        return cashAccount;
    }

    /**
     * 设置现金账号（单位：分）
     *
     * @param cashAccount 现金账号（单位：分）
     */
    public void setCashAccount(Integer cashAccount) {
        this.cashAccount = cashAccount;
    }

    /**
     * 获取赠送账号（单位：分）
     *
     * @return gift_account - 赠送账号（单位：分）
     */
    public Integer getGiftAccount() {
        return giftAccount;
    }

    /**
     * 设置赠送账号（单位：分）
     *
     * @param giftAccount 赠送账号（单位：分）
     */
    public void setGiftAccount(Integer giftAccount) {
        this.giftAccount = giftAccount;
    }

    /**
     * 获取折扣率（实际使用要除100）
     *
     * @return discount_rate - 折扣率（实际使用要除100）
     */
    public Byte getDiscountRate() {
        return discountRate;
    }

    /**
     * 设置折扣率（实际使用要除100）
     *
     * @param discountRate 折扣率（实际使用要除100）
     */
    public void setDiscountRate(Byte discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * 获取类型。1：普通；2：情侣
     *
     * @return type - 类型。1：普通；2：情侣
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型。1：普通；2：情侣
     *
     * @param type 类型。1：普通；2：情侣
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取状态。0：软删除；1:正常；2：冻结
     *
     * @return status - 状态。0：软删除；1:正常；2：冻结
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。0：软删除；1:正常；2：冻结
     *
     * @param status 状态。0：软删除；1:正常；2：冻结
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}