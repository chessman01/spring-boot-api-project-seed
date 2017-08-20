package com.tianbao.buy.domain;

import java.util.Date;
import javax.persistence.*;

public class Coach {
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
     * 名称
     */
    private String nick;

    /**
     * 手机
     */
    private String phone;

    /**
     * 二维码
     */
    @Column(name = "qr_code")
    private String qrCode;

    /**
     * 性别。1：男；0：女
     */
    private Byte sex;

    /**
     * 描述
     */
    private String description;

    /**
     * 头相
     */
    private String avatar;

    /**
     * 状态。0：软删除；1：正常；2：冻结
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
     * 获取名称
     *
     * @return nick - 名称
     */
    public String getNick() {
        return nick;
    }

    /**
     * 设置名称
     *
     * @param nick 名称
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * 获取手机
     *
     * @return phone - 手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机
     *
     * @param phone 手机
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取二维码
     *
     * @return qr_code - 二维码
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * 设置二维码
     *
     * @param qrCode 二维码
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * 获取性别。1：男；0：女
     *
     * @return sex - 性别。1：男；0：女
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * 设置性别。1：男；0：女
     *
     * @param sex 性别。1：男；0：女
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取头相
     *
     * @return avatar - 头相
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置头相
     *
     * @param avatar 头相
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取状态。0：软删除；1：正常；2：冻结
     *
     * @return status - 状态。0：软删除；1：正常；2：冻结
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。0：软删除；1：正常；2：冻结
     *
     * @param status 状态。0：软删除；1：正常；2：冻结
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}