package com.bit.module.pb.enums;

import lombok.AllArgsConstructor;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/2 10:15
 */
@AllArgsConstructor
public enum DisableTypeEenu {

    /**
     * 默认配置
     */
    DIE("1", "党员去世"),

    STOP("2", "停止党籍"),

    OUT("3", "党员出党"),

    OTHER("4", "其它");

    /**
     * 枚举值
     */
    private String value;
    /**
     * 枚举叙述
     */
    private String phrase;

    public static DisableTypeEenu getByValue(String value){
        for (DisableTypeEenu disableTypeMenu : values()) {
            if (disableTypeMenu.value.equals(value)) {
                return disableTypeMenu;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getPhrase() {
        return phrase;
    }

}
