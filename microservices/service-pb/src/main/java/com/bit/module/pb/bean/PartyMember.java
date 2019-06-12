package com.bit.module.pb.bean;

import com.bit.module.pb.enums.PMStatusEnum;
import com.bit.module.pb.vo.PartyMemberVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * PartyMember
 * @author generator
 */
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class PartyMember implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 姓名
     */	
	private String name;
    /**
     * 照片
     */	
	private String photo;
    /**
     * 性别
     */	
	private String sex;
    /**
     * 出生日期
     */	
	private Date birthdate;
    /**
     * 身份证号码
     */	
	private String idCard;
    /**
     * 民族
     */	
	private String nation;
    /**
     * 入党时间
     */	
	private Date joinTime;
    /**
     * 学历
     */	
	private String education;
    /**
     * 党员类型，1正式 2预备
     */	
	private Integer memberType;
    /**
     * 户籍所在派出所
     */	
	private String policeStation;
    /**
     * 工作/学习单位
     */	
	private String company;
    /**
     * 联系电话
     */	
	private String mobile;
    /**
     * 现居住地
     */	
	private String address;
    /**
     * 组织id
     */	
	private String orgId;
    /**
     * 组织名称[关联获取]
     */
	private String orgName;
    /**
     * 插入时间
     */	
	private Date insertTime;
    /**
     * 状态，0待完善 1正常 2停用
     */	
	private Integer status;

	//columns END

    /**
     *
     * @param partyMemberVO
     * @return
     */
    public static PartyMember buildPartyMember(PartyMemberVO partyMemberVO) {
        if (partyMemberVO != null) {
            PartyMember partyMember = new PartyMember();
            partyMember.setSex(partyMemberVO.getSex());
            partyMember.setPhoto(partyMemberVO.getPhoto());
            partyMember.setNation(partyMemberVO.getNation());
            partyMember.setEducation(partyMemberVO.getEducation());
            partyMember.setBirthdate(partyMemberVO.getBirthdate());
            partyMember.setMobile(partyMemberVO.getMobile());
            partyMember.setJoinTime(partyMemberVO.getJoinTime());
            partyMember.setMemberType(partyMemberVO.getMemberType());
            partyMember.setPoliceStation(partyMemberVO.getPoliceStation());
            partyMember.setCompany(partyMemberVO.getCompany());
            partyMember.setAddress(partyMemberVO.getAddress());
            if (partyMemberVO.getId() != null) {
                // 编辑
                partyMember.setId(partyMemberVO.getId());
                partyMember.setStatus(partyMemberVO.getStatus());
                // 待完善
                if(partyMemberVO.getStatus() == PMStatusEnum.DRAFT.getValue()) {
                    partyMember.setName(partyMemberVO.getName());
                    partyMember.setIdCard(partyMemberVO.getIdCard());
                    partyMember.setStatus(judgeStatus(partyMemberVO));
                }
                return partyMember;
            }
            // 姓名
            partyMember.setName(partyMemberVO.getName());
            // 身份证
            partyMember.setIdCard(partyMemberVO.getIdCard());
            // 党支部
            partyMember.setOrgId(partyMemberVO.getOrgId());
            partyMember.setInsertTime(new Date());
            partyMember.setStatus(judgeStatus(partyMemberVO));
            return partyMember;
        }
        return null;
    }

    public static Integer judgeStatus(PartyMemberVO vo) {
        // 正常
        int result = 1;
        if (StringUtils.isEmpty(vo.getJoinTime()))
            return 0;
        if (StringUtils.isEmpty(vo.getIdCard()))
            return 0;
        if (StringUtils.isEmpty(vo.getMobile()))
            return 0;
        if (StringUtils.isEmpty(vo.getNation()))
            return 0;
        if (StringUtils.isEmpty(vo.getEducation()))
            return 0;
        if (StringUtils.isEmpty(vo.getPoliceStation()))
            return 0;
        if (StringUtils.isEmpty(vo.getCompany()))
            return 0;
        if (StringUtils.isEmpty(vo.getAddress()))
            return 0;
        return result;
    }

    public static Integer judgeStatus(PartyMember partyMember) {
        // 正常
        int result = 1;
        if (StringUtils.isEmpty(partyMember.getName()))
            return 0;
        if (StringUtils.isEmpty(partyMember.getJoinTime()))
            return 0;
        if (StringUtils.isEmpty(partyMember.getIdCard()))
            return 0;
        if (StringUtils.isEmpty(partyMember.getMobile()))
            return 0;
        if (StringUtils.isEmpty(partyMember.getNation()))
            return 0;
        if (StringUtils.isEmpty(partyMember.getEducation()))
            return 0;
        if (StringUtils.isEmpty(partyMember.getPoliceStation()))
            return 0;
        if (StringUtils.isEmpty(partyMember.getCompany()))
            return 0;
        if (StringUtils.isEmpty(partyMember.getAddress()))
            return 0;
        return result;
    }

}


