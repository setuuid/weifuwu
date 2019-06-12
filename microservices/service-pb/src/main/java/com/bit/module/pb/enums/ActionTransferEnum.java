package com.bit.module.pb.enums;

import lombok.AllArgsConstructor;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/8 13:15
 */
@AllArgsConstructor
public enum ActionTransferEnum {

    /**
     * 动作 对于党员信息的主题：1新增 2停用 3启用；
     * 对于党员组织关系转移的主题：1转移 2转入 3转出 4转出到镇外 5转入到镇内
     */
    TRANSFER(1, "转移"),

    SHIFTTO(2, "转入"),

    ROLLOUT(3, "转出"),

    OUTSIDE(4, "转出镇外"),

    INTOWN(5, "转入到镇内");

    /**
     * 枚举值
     */
    private int value;
    /**
     * 枚举叙述
     */
    private String phrase;

    public static ActionTransferEnum getByValue(int value){
        for (ActionTransferEnum actionTransferEnum : values()) {
            if (actionTransferEnum.value == value) {
                return actionTransferEnum;
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
