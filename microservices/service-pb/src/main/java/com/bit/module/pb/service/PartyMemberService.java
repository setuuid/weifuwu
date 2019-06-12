package com.bit.module.pb.service;

import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * PartyMember的Service
 *
 * @author codeGenerator
 */
public interface PartyMemberService {

    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberVO
     * @return
     */
    PageInfo findByConditionPage(PartyMemberQuery partyMemberVO);

    /**
     * 获取组织集合的正式党员信息
     *
     * @param query
     * @return
     */
    PageInfo<PartyMember> findByOrgIdsPage(PartyMemberQuery query);

    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberVO
     * @return
     */
    PageInfo findByConditionPage2(PartyMemberParamsVO partyMemberVO);

    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberVO
     * @return
     */
    PageInfo findByConditionPage3(PartyMemberParamsVO partyMemberVO);


    /**
     * 查询退伍党员
     *
     * @param partyMemberExServicemanVO
     * @return
     */
    PageInfo findExServicemanPage(PartyMemberExServicemanVO partyMemberExServicemanVO);

    /**
     * 查询所有PartyMember
     *
     * @param partyMember
     * @return
     */
    List<PartyMemberExportVO> findAll(PartyMemberParamsVO partyMember);

    /**
     * 导出退伍党员
     *
     * @param exServicemanParamVO
     * @return
     */
    List<PartyMemberExportVO> findExServiceman(ExServicemanParamVO exServicemanParamVO);

    /**
     * 导出停用党员信息
     *
     * @param partyMemberVO
     * @return
     */
    List<DisablePartyExportVO> findDisableParty(PartyMemberVO partyMemberVO);

    /**
     * 通过主键查询单个PartyMember
     *
     * @param id
     * @return
     */
    PartyMember findById(Long id);

    /**
     * 根据身份证获取党员信息
     *
     * @param idCard
     * @return
     */
    PartyMember findByIdCard(String idCard);

    /**
     * 根据身份证集合获取党员信息
     *
     * @param idCards
     * @return
     */
    List<PartyMember> findByIdCards(List<String> idCards);

    /**
     * 批量保存PartyMember
     *
     * @param partyMembers
     */
    void batchAdd(List<PartyMember> partyMembers);

    /**
     * 保存PartyMember
     *
     * @param partyMember
     */
    PartyMember add(PartyMemberVO partyMember);

    /**
     * 批量更新PartyMember
     *
     * @param partyMembers
     */
    void batchUpdate(List<PartyMember> partyMembers);

    /**
     * 更新PartyMember
     *
     * @param partyMember
     */
    void update(PartyMemberVO partyMember);

    /**
     * 完善PartyMember
     *
     * @param partyMember
     */
    void perfect(PartyMemberVO partyMember);

    /**
     * 删除PartyMember
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 批量删除PartyMember
     *
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 是否存在党员，存在返回true，否则返回false
     * @param name
     * @param idCard
     * @return
     */
    Boolean existPartyMember(String name, String idCard);

    /**
     * 根据身份证查镇内党员
     * @param toQuery
     * @return
     */
    PartyMember findByIdCardAndInside(PartyMember toQuery);
}
