package com.bit.module.pb.enums;

import lombok.AllArgsConstructor;

/**
 * @autor xiaoyu.fang
 * @date 2018/12/28 13:49
 */
@AllArgsConstructor
public enum PMStatusEnum {
    /**
     * 默认配置
     */
    DRAFT(0, "待完善"),

    NORMAL(1, "正常"),

    DISABLE(2, "停用"),

    EMIGRATION(3, "转出");

    /**
     * 枚举值
     */
    private int value;
    /**
     * 枚举叙述
     */
    private String phrase;

    public static PMStatusEnum getByValue(int value){
        for (PMStatusEnum pmStatusEnum : values()) {
            if (pmStatusEnum.value == value) {
                return pmStatusEnum;
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
