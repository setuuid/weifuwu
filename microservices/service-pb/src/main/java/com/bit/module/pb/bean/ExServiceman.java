package com.bit.module.pb.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * ExServiceman
 * @author generator
 */
@Data
@EqualsAndHashCode
public class ExServiceman implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 籍贯
     */	
	private String origin;
    /**
     * 原服役部队
     */	
	private String originalTroops;
    /**
     * 退役时间
     */	
	private Date retireTime;
    /**
     * 是否自主择业，0否 1是
     */	
	private Integer isSelfEmployment;
    /**
     * 组织关系落实时间
     */	
	private Date relTransferTime;
    /**
     * 编入党支部
     */	
	private String orgName;
    /**
     * 党员id
     */	
	private Long memberId;

	//columns END

}


