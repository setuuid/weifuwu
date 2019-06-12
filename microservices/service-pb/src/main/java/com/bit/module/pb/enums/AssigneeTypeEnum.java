package com.bit.module.pb.enums;

import lombok.AllArgsConstructor;

/**
 * @autor xiaoyu.fang
 * @date 2018/12/27 11:18
 */
@AllArgsConstructor
public enum AssigneeTypeEnum {
    /**
     * 默认配置
     */
    ORGANIZEID(1, "组织ID"),

    USERID(2, "用户ID"),

    ROLE(3, "角色");

    /**
     * 枚举值
     */
    private int value;
    /**
     * 枚举叙述
     */
    private String phrase;

    public static AssigneeTypeEnum getByValue(int value){
        for (AssigneeTypeEnum assigneeTypeEnum : values()) {
            if (assigneeTypeEnum.value == value) {
                return assigneeTypeEnum;
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
