package com.tianbao.buy.model;

import java.util.Date;
import javax.persistence.*;

public class Tag {
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
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 状态。0：软删除；1：正常
     */
    private Byte status;

    /**
     * 类型。0：通用；1：课程
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
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取描述
     *
     * @return desc - 描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置描述
     *
     * @param desc 描述
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取状态。0：软删除；1：正常
     *
     * @return status - 状态。0：软删除；1：正常
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。0：软删除；1：正常
     *
     * @param status 状态。0：软删除；1：正常
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取类型。0：通用；1：课程
     *
     * @return type - 类型。0：通用；1：课程
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型。0：通用；1：课程
     *
     * @param type 类型。0：通用；1：课程
     */
    public void setType(Byte type) {
        this.type = type;
    }
}