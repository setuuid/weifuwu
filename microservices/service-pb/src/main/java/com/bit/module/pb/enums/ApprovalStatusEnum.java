package com.bit.module.pb.enums;

import lombok.AllArgsConstructor;

/**
 * @autor xiaoyu.fang
 * @date 2018/12/26 13:21
 */
@AllArgsConstructor
public enum ApprovalStatusEnum {
    /**
     * 默认配置
     */
    DRAFT(0, "草稿"),

    AUDIT(1, "审核中"),

    PASSED(2, "已通过"),

    RETURN(3, "已退回"),

    RECEIVE(4, "待接收"),

    UNRECEIVE(5, "未接受");

    /**
     * 枚举值
     */
    private int value;
    /**
     * 枚举叙述
     */
    private String phrase;

    public static ApprovalStatusEnum getByValue(int value){
        for (ApprovalStatusEnum approvalStatusEnum : values()) {
            if (approvalStatusEnum.value == value) {
                return approvalStatusEnum;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getPhrase() {
        return phrase;
    }
}
