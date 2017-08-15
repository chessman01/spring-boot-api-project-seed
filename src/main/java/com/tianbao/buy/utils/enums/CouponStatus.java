package com.tianbao.buy.utils.enums;

public enum CouponStatus implements EnumMessage{
    // 状态。0：软删除；1：未使用；2：已使用；3：已过期
    SOFT_DEL((byte)0, "已删除"),
    NORMAL((byte)1, "未使用"),
    USED((byte)2, "已使用"),
    EXPIRED((byte)3, "已过期");

    public byte code;

    public String desc;

    CouponStatus(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    @Override
    public Object getValue() {
        //此处需要根据枚举对象的哪个属性返回枚举对象，就return该属性
        return code;
    }
}
