package com.bit.module.pb.vo;

import com.bit.module.pb.bean.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/8 17:49
 */
@Data
public class PartyMemberDetailVO implements Serializable{

    private PartyMember partyMember;

    private List<RelationshipTransfer> relationshipTransfers;

    private ExServiceman exServiceman;

    private List<PartyDue> partyDues;

    private PartyMemberApproval approval;
}
