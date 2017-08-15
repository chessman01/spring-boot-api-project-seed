package com.tianbao.buy.domain;

import java.util.Date;
import javax.persistence.*;

public class User {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改日期
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 微信openId
     */
    @Column(name = "wx_open_id")
    private String wxOpenId;

    /**
     * 微信unionId
     */
    @Column(name = "wx_union_id")
    private String wxUnionId;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名
     */
    private String nick;

    /**
     * 性别
     */
    private Boolean sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 头相地址
     */
    private String avatar;

    /**
     * 运动总时长（单位：分）
     */
    @Column(name = "duration_total")
    private Integer durationTotal;

    /**
     * 运动周总时长（单位：分）
     */
    @Column(name = "duration_week")
    private Integer durationWeek;

    /**
     * 卡路时消耗（单位：卡）
     */
    @Column(name = "calorie_total")
    private Integer calorieTotal;

    /**
     * 状态。1：正常；0：软删除
     */
    private Byte status;

    /**
     * 积分。
     */
    private Long point;

    /**
     * 引荐人ID
     */
    @Column(name = "referrer_id")
    private Long referrerId;

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
     * 获取创建日期
     *
     * @return create_time - 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改日期
     *
     * @return modify_time - 修改日期
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改日期
     *
     * @param modifyTime 修改日期
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取微信openId
     *
     * @return wx_open_id - 微信openId
     */
    public String getWxOpenId() {
        return wxOpenId;
    }

    /**
     * 设置微信openId
     *
     * @param wxOpenId 微信openId
     */
    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    /**
     * 获取微信unionId
     *
     * @return wx_union_id - 微信unionId
     */
    public String getWxUnionId() {
        return wxUnionId;
    }

    /**
     * 设置微信unionId
     *
     * @param wxUnionId 微信unionId
     */
    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取用户名
     *
     * @return nick - 用户名
     */
    public String getNick() {
        return nick;
    }

    /**
     * 设置用户名
     *
     * @param nick 用户名
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * 获取性别
     *
     * @return sex - 性别
     */
    public Boolean getSex() {
        return sex;
    }

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取手机号码
     *
     * @return phone - 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号码
     *
     * @param phone 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取头相地址
     *
     * @return avatar - 头相地址
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置头相地址
     *
     * @param avatar 头相地址
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取运动总时长（单位：分）
     *
     * @return duration_total - 运动总时长（单位：分）
     */
    public Integer getDurationTotal() {
        return durationTotal;
    }

    /**
     * 设置运动总时长（单位：分）
     *
     * @param durationTotal 运动总时长（单位：分）
     */
    public void setDurationTotal(Integer durationTotal) {
        this.durationTotal = durationTotal;
    }

    /**
     * 获取运动周总时长（单位：分）
     *
     * @return duration_week - 运动周总时长（单位：分）
     */
    public Integer getDurationWeek() {
        return durationWeek;
    }

    /**
     * 设置运动周总时长（单位：分）
     *
     * @param durationWeek 运动周总时长（单位：分）
     */
    public void setDurationWeek(Integer durationWeek) {
        this.durationWeek = durationWeek;
    }

    /**
     * 获取卡路时消耗（单位：卡）
     *
     * @return calorie_total - 卡路时消耗（单位：卡）
     */
    public Integer getCalorieTotal() {
        return calorieTotal;
    }

    /**
     * 设置卡路时消耗（单位：卡）
     *
     * @param calorieTotal 卡路时消耗（单位：卡）
     */
    public void setCalorieTotal(Integer calorieTotal) {
        this.calorieTotal = calorieTotal;
    }

    /**
     * 获取状态。1：正常；0：软删除
     *
     * @return status - 状态。1：正常；0：软删除
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。1：正常；0：软删除
     *
     * @param status 状态。1：正常；0：软删除
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取积分。
     *
     * @return point - 积分。
     */
    public Long getPoint() {
        return point;
    }

    /**
     * 设置积分。
     *
     * @param point 积分。
     */
    public void setPoint(Long point) {
        this.point = point;
    }

    /**
     * 获取引荐人ID
     *
     * @return referrer_id - 引荐人ID
     */
    public Long getReferrerId() {
        return referrerId;
    }

    /**
     * 设置引荐人ID
     *
     * @param referrerId 引荐人ID
     */
    public void setReferrerId(Long referrerId) {
        this.referrerId = referrerId;
    }
}