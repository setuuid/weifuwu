package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/2 10:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PartyMemberParamsVO extends BasePageVo implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 1正式2预备
     */
    private Integer memberType;

    /**
     * 原组织
     */
    private String fromOrgName;

    /**
     * 目标组织名称
     */
    private String toOrgName;

    /**
     * 停用原因
     * 1：党员去世；
     * 2：停止党籍；
     * 3：党员出党；
     * 4：其它；
     */
    private String reason;

    /**
     * 状态
     * 0：待完善；
     * 1：正常；
     * 2：停用；
     * 3：转移；
     */
    private List<Integer> status;

}
