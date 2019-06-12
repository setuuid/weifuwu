package com.bit.module.pb.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Organization
 * @author generator
 */
@Data
public class Organization implements Serializable {

	//columns START

    /**
     * id
     */	
	private String id;
    /**
     * 代码
     */
    private String pCode;
    /**
     * 名称
     */	
	private String name;
    /**
     * 组织类型
     */
	private Integer orgType;
    /**
     * 组织描述
     */
	private String orgDesc;
    /**
     * 顺序
     */	
	private Integer sort;
    /**
     * 是否审批机关，0否 1是
     */	
	private Integer isApprovalAuz;
    /**
     * 是否已删除，0否 1是
     */	
	private Integer status;

	//columns END

}


