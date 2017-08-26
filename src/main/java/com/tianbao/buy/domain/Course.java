package com.tianbao.buy.domain;

import java.util.Date;
import javax.persistence.*;

public class Course {
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
     * 标题
     */
    private String title;

    /**
     * 地址ID
     */
    @Column(name = "address_id")
    private Integer addressId;

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
     * 价格
     */
    private Integer price;

    /**
     * 描述
     */
    private String description;

    /**
     * 暂不用
     */
    @Column(name = "tag_ids")
    private String tagIds;

    @Column(name = "main_pic1")
    private String mainPic1;

    @Column(name = "main_pic2")
    private String mainPic2;

    @Column(name = "main_pic3")
    private String mainPic3;

    @Column(name = "main_pic4")
    private String mainPic4;

    @Column(name = "main_pic5")
    private String mainPic5;

    @Column(name = "sub_pic1")
    private String subPic1;

    @Column(name = "sub_pic2")
    private String subPic2;

    @Column(name = "sub_pic3")
    private String subPic3;

    @Column(name = "sub_pic4")
    private String subPic4;

    @Column(name = "sub_pic5")
    private String subPic5;

    /**
     * 锻炼效果
     */
    @Column(name = "training_effect")
    private String trainingEffect;

    /**
     * 适合人群
     */
    private String crowd;

    /**
     * 问答
     */
    private String faq;

    /**
     * 注意事项
     */
    private String care;

    /**
     * 教练id
     */
    @Column(name = "coach_id")
    private Long coachId;

    /**
     * 状态。0：软删除；1：正常；2:过期
     */
    private Byte status;

    /**
     * 库存数。
     */
    private Short stock;

    /**
     * 标签
     */
    private String tags;

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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取地址ID
     *
     * @return address_id - 地址ID
     */
    public Integer getAddressId() {
        return addressId;
    }

    /**
     * 设置地址ID
     *
     * @param addressId 地址ID
     */
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
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
     * 获取价格
     *
     * @return price - 价格
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(Integer price) {
        this.price = price;
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
     * 获取暂不用
     *
     * @return tag_ids - 暂不用
     */
    public String getTagIds() {
        return tagIds;
    }

    /**
     * 设置暂不用
     *
     * @param tagIds 暂不用
     */
    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    /**
     * @return main_pic1
     */
    public String getMainPic1() {
        return mainPic1;
    }

    /**
     * @param mainPic1
     */
    public void setMainPic1(String mainPic1) {
        this.mainPic1 = mainPic1;
    }

    /**
     * @return main_pic2
     */
    public String getMainPic2() {
        return mainPic2;
    }

    /**
     * @param mainPic2
     */
    public void setMainPic2(String mainPic2) {
        this.mainPic2 = mainPic2;
    }

    /**
     * @return main_pic3
     */
    public String getMainPic3() {
        return mainPic3;
    }

    /**
     * @param mainPic3
     */
    public void setMainPic3(String mainPic3) {
        this.mainPic3 = mainPic3;
    }

    /**
     * @return main_pic4
     */
    public String getMainPic4() {
        return mainPic4;
    }

    /**
     * @param mainPic4
     */
    public void setMainPic4(String mainPic4) {
        this.mainPic4 = mainPic4;
    }

    /**
     * @return main_pic5
     */
    public String getMainPic5() {
        return mainPic5;
    }

    /**
     * @param mainPic5
     */
    public void setMainPic5(String mainPic5) {
        this.mainPic5 = mainPic5;
    }

    /**
     * @return sub_pic1
     */
    public String getSubPic1() {
        return subPic1;
    }

    /**
     * @param subPic1
     */
    public void setSubPic1(String subPic1) {
        this.subPic1 = subPic1;
    }

    /**
     * @return sub_pic2
     */
    public String getSubPic2() {
        return subPic2;
    }

    /**
     * @param subPic2
     */
    public void setSubPic2(String subPic2) {
        this.subPic2 = subPic2;
    }

    /**
     * @return sub_pic3
     */
    public String getSubPic3() {
        return subPic3;
    }

    /**
     * @param subPic3
     */
    public void setSubPic3(String subPic3) {
        this.subPic3 = subPic3;
    }

    /**
     * @return sub_pic4
     */
    public String getSubPic4() {
        return subPic4;
    }

    /**
     * @param subPic4
     */
    public void setSubPic4(String subPic4) {
        this.subPic4 = subPic4;
    }

    /**
     * @return sub_pic5
     */
    public String getSubPic5() {
        return subPic5;
    }

    /**
     * @param subPic5
     */
    public void setSubPic5(String subPic5) {
        this.subPic5 = subPic5;
    }

    /**
     * 获取锻炼效果
     *
     * @return training_effect - 锻炼效果
     */
    public String getTrainingEffect() {
        return trainingEffect;
    }

    /**
     * 设置锻炼效果
     *
     * @param trainingEffect 锻炼效果
     */
    public void setTrainingEffect(String trainingEffect) {
        this.trainingEffect = trainingEffect;
    }

    /**
     * 获取适合人群
     *
     * @return crowd - 适合人群
     */
    public String getCrowd() {
        return crowd;
    }

    /**
     * 设置适合人群
     *
     * @param crowd 适合人群
     */
    public void setCrowd(String crowd) {
        this.crowd = crowd;
    }

    /**
     * 获取问答
     *
     * @return faq - 问答
     */
    public String getFaq() {
        return faq;
    }

    /**
     * 设置问答
     *
     * @param faq 问答
     */
    public void setFaq(String faq) {
        this.faq = faq;
    }

    /**
     * 获取注意事项
     *
     * @return care - 注意事项
     */
    public String getCare() {
        return care;
    }

    /**
     * 设置注意事项
     *
     * @param care 注意事项
     */
    public void setCare(String care) {
        this.care = care;
    }

    /**
     * 获取教练id
     *
     * @return coach_id - 教练id
     */
    public Long getCoachId() {
        return coachId;
    }

    /**
     * 设置教练id
     *
     * @param coachId 教练id
     */
    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    /**
     * 获取状态。0：软删除；1：正常；2:过期
     *
     * @return status - 状态。0：软删除；1：正常；2:过期
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态。0：软删除；1：正常；2:过期
     *
     * @param status 状态。0：软删除；1：正常；2:过期
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取库存数。
     *
     * @return stock - 库存数。
     */
    public Short getStock() {
        return stock;
    }

    /**
     * 设置库存数。
     *
     * @param stock 库存数。
     */
    public void setStock(Short stock) {
        this.stock = stock;
    }

    /**
     * 获取标签
     *
     * @return tags - 标签
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置标签
     *
     * @param tags 标签
     */
    public void setTags(String tags) {
        this.tags = tags;
    }
}