package com.bit.module.pb.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/12 17:13
 */
@Data
public class ExServicemanParamVO implements Serializable {

    private String orgId;

    private Integer isSelfEmployment;

    private String name;

    private String orgName;

    private String originalTroops;
}
