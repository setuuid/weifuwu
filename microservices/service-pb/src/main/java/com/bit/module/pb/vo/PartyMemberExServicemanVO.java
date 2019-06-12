package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/2 15:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PartyMemberExServicemanVO extends BasePageVo {

    /**
     * 姓名
     */
    private String name;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 原服役部队
     */
    private String originalTroops;
    /**
     * 是否自主择业，0否1是
     */
    private Integer isSelfEmployment;
}
