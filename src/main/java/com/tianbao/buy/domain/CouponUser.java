package com.tianbao.buy.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "coupon_user")
public class CouponUser {
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
     * 礼券模版ID
     */
    @Column(name = "coupon_template_id")
    private Long couponTemplateId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 状态。0：删除；1：正常；2：已过期；3：已使用
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
     * 获取礼券模版ID
     *
     * @return coupon_template_id - 礼券模版ID
     */
    public Long getCouponTemplateId() {
        return couponTemplateId;
    }

    /**
     * 设置礼券模版ID
     *
     * @param couponTemplateId 礼券模版ID
     */
    public void setCouponTemplateId(Long couponTemplateId) {
        this.couponTemplateId = couponTemplateId;
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
     * 获取状态。0：删除；1：正常；2：已过期；3：已使用
     *
     * @return status - 状态。0：删除；1：正常；2：已过期；3：已使用
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。0：删除；1：正常；2：已过期；3：已使用
     *
     * @param status 状态。0：删除；1：正常；2：已过期；3：已使用
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}