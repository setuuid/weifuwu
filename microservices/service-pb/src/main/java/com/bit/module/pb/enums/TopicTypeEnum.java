package com.bit.module.pb.enums;

import lombok.AllArgsConstructor;

/**
 * @autor xiaoyu.fang
 * @date 2018/12/27 11:04
 */
@AllArgsConstructor
public enum TopicTypeEnum {
    /**
     * 默认配置
     */
    PARTYMEMBER(1, "党员信息审批"),

    TRANSFER(2, "党员组织关系转移审批");

    /**
     * 枚举值
     */
    private int value;
    /**
     * 枚举叙述
     */
    private String phrase;

    public static TopicTypeEnum getByValue(int value){
        for (TopicTypeEnum todoTypeEnum : values()) {
            if (todoTypeEnum.value == value) {
                return todoTypeEnum;
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
