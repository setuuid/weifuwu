package com.bit.module.pb.enums;

import lombok.AllArgsConstructor;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/4 9:48
 */
@AllArgsConstructor
public enum TransferTypeMenu {

    /**
     * 默认配置
     */
    BETWEEN(1, "镇内互转"),

    OUT(2, "镇内转镇外"),

    IN(3, "镇外转镇内");

    /**
     * 枚举值
     */
    private int value;
    /**
     * 枚举叙述
     */
    private String phrase;

    public static TransferTypeMenu getByValue(int value){
        for (TransferTypeMenu transferTypeMenu : values()) {
            if (transferTypeMenu.value == value) {
                return transferTypeMenu;
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
