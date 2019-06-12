package com.bit.module.pb.enums;

import lombok.AllArgsConstructor;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/8 14:00
 */
@AllArgsConstructor
public enum ActionPartyMemberEnum {
    // 1新增 2停用 3启用；

    ADD(1, "新增"),

    DISABLE(2, "停用"),

    ENABLED(3, "启用");

    /**
     * 枚举值
     */
    private int value;
    /**
     * 枚举叙述
     */
    private String phrase;

    public static ActionPartyMemberEnum getByValue(int value){
        for (ActionPartyMemberEnum actionPartyMemberEnum : values()) {
            if (actionPartyMemberEnum.value == value) {
                return actionPartyMemberEnum;
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
