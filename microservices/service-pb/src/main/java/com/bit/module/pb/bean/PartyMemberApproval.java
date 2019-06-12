package com.bit.module.pb.bean;

import com.bit.module.pb.enums.ActionPartyMemberEnum;
import com.bit.module.pb.enums.ApprovalStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 党员信息审核表
 *
 * PartyMemberApproval
 * @author generator
 */
@Data
public class PartyMemberApproval implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 申请类型，1新增党员 2停用党员 3启用党员
     */	
	private Integer type;
    /**
     * 申请人，用户id
     */	
	private Long userId;
    /**
     * 申请人所在组织id
     */	
	private String orgId;
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
     * 状态，0草稿 1审核中 2已通过 3已退回 4等待接收（镇外） 5未接受（镇外）
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

    /**
     * 回填信息
     * @param partyMemberApproval
     * @param partyMember
     * @return
     */
    public static PartyMemberApproval buildPartyMemberApproval(PartyMemberApproval partyMemberApproval, PartyMember partyMember) {
        if (partyMember != null) {
            partyMemberApproval.setUserId(partyMember.getId());
            partyMemberApproval.setOrgId(partyMember.getOrgId());
            partyMemberApproval.setOrgName(partyMember.getOrgName());
            partyMemberApproval.setMemberId(partyMember.getId());
            partyMemberApproval.setInsertTime(new Date());
            partyMemberApproval.setStatus(ApprovalStatusEnum.DRAFT.getValue());
            // json
            PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
            partyMemberSummary.setName(partyMember.getName());
            partyMemberSummary.setMobile(partyMember.getMobile());
            partyMemberSummary.setMemberType(partyMember.getMemberType());
            partyMemberApproval.setModification(partyMemberSummary);
            return partyMemberApproval;
        }
        return null;
    }

    /**
     * 新增党员
     * 回填信息
     * @param partyMember
     * @return
     */
    public static PartyMemberApproval buildPartyMemberApproval(PartyMemberSummary partyMember) {
        if (partyMember != null) {
            PartyMemberApproval partyMemberApproval = new PartyMemberApproval();
            partyMemberApproval.setOrgId(partyMember.getOrgId());
            partyMemberApproval.setOrgName(partyMember.getOrgName());
            partyMemberApproval.setType(ActionPartyMemberEnum.ADD.getValue());
            partyMemberApproval.setStatus(ApprovalStatusEnum.DRAFT.getValue());
            partyMemberApproval.setInsertTime(new Date());
            // 配置json
            partyMemberApproval.setModification(partyMember);
            if (partyMember.getId() != null) {
                // 不更新添加时间
                partyMemberApproval.setInsertTime(null);
                partyMemberApproval.setId(partyMember.getId());
                return partyMemberApproval;
            }
            return partyMemberApproval;
        }
        return null;
    }

}


