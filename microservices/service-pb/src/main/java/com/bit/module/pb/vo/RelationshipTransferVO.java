package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.pb.bean.PartyMemberSummary;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * RelationshipTransfer
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RelationshipTransferVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 党员id
     */	
	private Long memberId;
    /**
     * 党员名称
     */
    private String memberName;
    /**
     * 原组织id
     */	
	private String fromOrgId;
    /**
     * 原组织名称
     */	
	private String fromOrgName;
    /**
     * 转出原组织时间
     */	
	private Date outTime;
    /**
     * 目标组织id
     */	
	private String toOrgId;
    /**
     * 目标组织名称
     */	
	private String toOrgName;
    /**
     * 转入目标组织时间
     */	
	private Date inTime;
    /**
     * 附件列表，英文逗号分隔。附件id为文件服务的文件id
     */	
	private String attachmentIds;
    /**
     * 插入时间
     */	
	private Date insertTime;
    /**
     * 完成时间
     */	
	private Date completeTime;
    /**
     * 状态，0草稿 1审核中 2已通过 3已退回 4等待接收（镇外） 5确认接收（镇外）6未接受（镇外）
     */	
	private Integer status;

    /**
     * 需要修改的党员信息
     */
    private PartyMemberSummary modification;

    /**
     * 党员党费已交至时间
     */
    private String deadline;

	//columns END

}


