package com.bit.module.pb.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/7 16:25
 */
@Data
public class OrganizationInfoVO implements Serializable {
    private String id;
    /**
     * 名称
     */
    private String name;
}
