package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.pb.bean.Done;
import com.bit.module.pb.bean.PartyMemberApproval;
import com.bit.module.pb.enums.TopicTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * Done
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DoneVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 主题，1党员信息 2党员组织关系转移
     */	
	private Integer topic;
    /**
     * 动作 对于党员信息的主题：1新增 2停用 3启用； 对于党员组织关系转移的主题：1转移 2转入 3转出 4转出到镇外 5转入到镇内
     */	
	private Integer action;
    /**
     * 关联id
     */	
	private Long correlationId;
    /**
     * 内容
     */	
	private String content;
    /**
     * 上一处理方名称，组织（或用户）名称
     */	
	private String lastHandlerName;
    /**
     * 处理人，用户id
     */	
	private Long userId;
    /**
     * 处理人所在组织id
     */	
	private Long orgId;
    /**
     * 处理人所在组织名称
     */	
	private String orgName;
    /**
     * 处理原因
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
     * 提交时间
     */	
	private Date submitTime;
    /**
     * 处理时间
     */	
	private Date handleTime;

	//columns END

    public static Done buildDone(PartyMemberApproval partyMemberApproval, Map<String, Object> userParam, String content){
        if (partyMemberApproval != null) {
            Done done = new Done();
            done.setOrgId(partyMemberApproval.getOrgId());
            done.setOrgName(partyMemberApproval.getOrgName());

            done.setUserId(Long.valueOf(userParam.get("userId").toString()));
            done.setContent(content);
            done.setCorrelationId(partyMemberApproval.getId());
            done.setLastHandlerName(partyMemberApproval.getOrgName());

            // 主题
            done.setTopic(TopicTypeEnum.PARTYMEMBER.getValue());
            done.setAction(partyMemberApproval.getType());

            done.setHandleTime(new Date());
            return done;
        }
        return null;
    }

}


