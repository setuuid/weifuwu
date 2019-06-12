package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.pb.bean.PartyMemberSummary;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * PartyMemberApproval
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PartyMemberApprovalVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 申请类型，1新增党员2停用党员3启用党员
     */	
	private String type;
    /**
     * 申请人，用户id
     */	
	private Long userId;
    /**
     * 申请人所在组织id
     */	
	private Long orgId;
    /**
     * 申请原因
     */	
	private String reason;
    /**
     * 备注
     */	
	private String remark;
    /**
     * 附件id列表，英文逗号分隔。附件id为文件服务的文件id
     */	
	private String attachmentIds;
    /**
     * 党员id
     */	
	private Long memberId;
    /**
     * 需要修改的党员信息
     */	
	private PartyMemberSummary modification;
    /**
     * 插入时间
     */	
	private Date insertTime;
    /**
     * 完成时间
     */	
	private Date completeTime;
    /**
     * 状态，0草稿 1审核中 2已通过 3已退回
     */	
	private Integer status;
	// json 关联
    /**
     * 党员名称
     */
	private String memberName;
    /**
     * 党员所在组织名称
     */
    private String orgName;

	//columns END

}


