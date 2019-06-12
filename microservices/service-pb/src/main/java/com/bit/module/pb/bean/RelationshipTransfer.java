package com.bit.module.pb.bean;

import com.bit.module.pb.enums.ApprovalStatusEnum;
import com.bit.module.pb.vo.RelationshipTransferVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * RelationshipTransfer
 * @author generator
 */
@Data
public class RelationshipTransfer implements Serializable {

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
     * 申请人，用户id
     */
    private Long userId;
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
     * 身份证
     */
	private String idCard;

    /**
     * 党员党费已交至时间
     */
    private String deadline;

    /**
     * [与数据库无关]
     * 是否退伍军人
     * 1：是；
     * 0：否
     */
    private Integer isExServiceman;

    /**
     * 需要修改的党员信息
     */
    private PartyMemberSummary modification;

	//columns END

    /**
     * 新增党员信息转移
     * @param vo
     * @return
     */
    public static RelationshipTransfer buildRelationshipTransfer(RelationshipTransferVO vo, PartyMember partyMember) {
        if (vo != null) {
            RelationshipTransfer relationshipTransfer = new RelationshipTransfer();
            relationshipTransfer.setMemberId(partyMember.getId());
            relationshipTransfer.setMemberName(partyMember.getName());
            relationshipTransfer.setFromOrgId(partyMember.getOrgId());
            relationshipTransfer.setFromOrgName(partyMember.getOrgName());

            relationshipTransfer.setToOrgId(vo.getToOrgId());
            relationshipTransfer.setToOrgName(vo.getToOrgName());

            relationshipTransfer.setIdCard(partyMember.getIdCard());
            relationshipTransfer.setStatus(ApprovalStatusEnum.DRAFT.getValue());
            relationshipTransfer.setAttachmentIds(vo.getAttachmentIds());

            relationshipTransfer.setDeadline(vo.getDeadline());
            if (vo.getId() != null) {
                relationshipTransfer.setId(vo.getId());
                return relationshipTransfer;
            }
            relationshipTransfer.setOutTime(new Date());
            relationshipTransfer.setInsertTime(new Date());
            return relationshipTransfer;
        }
        return null;
    }

    /**
     * 接收镇外党员转移
     * @param vo
     * @return
     */
    public static RelationshipTransfer buildRelationshipTransfer(PartyMemberSummary vo) {
        if (vo != null) {
            RelationshipTransfer relationshipTransfer = new RelationshipTransfer();
            relationshipTransfer.setMemberName(vo.getName());
            relationshipTransfer.setIdCard(vo.getIdCard());

            relationshipTransfer.setFromOrgId(vo.getFromOrgId());
            relationshipTransfer.setFromOrgName(vo.getFromOrgName());
            relationshipTransfer.setToOrgId(vo.getOrgId());
            relationshipTransfer.setToOrgName(vo.getOrgName());

            relationshipTransfer.setAttachmentIds(vo.getAttachmentIds());
            relationshipTransfer.setIsExServiceman(vo.getExServiceman() == null ? 0 : 1);

            relationshipTransfer.setAttachmentIds(vo.getAttachmentIds());
            relationshipTransfer.setStatus(ApprovalStatusEnum.DRAFT.getValue());

            relationshipTransfer.setModification(vo);

            if (vo.getId() != null) {
                relationshipTransfer.setId(vo.getId());
                return relationshipTransfer;
            }
            relationshipTransfer.setOutTime(new Date());
            relationshipTransfer.setInsertTime(new Date());
            return relationshipTransfer;
        }
        return null;
    }
}


