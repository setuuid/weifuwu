package com.bit.module.pb.dao;

import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.bean.PartyMemberExServiceman;
import com.bit.module.pb.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PartyMember管理的Dao
 *
 * @author
 */
@Repository
public interface PartyMemberDao {
    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberVO
     * @return
     */
    List<PartyMember> findByConditionPage(PartyMemberQuery partyMemberVO);

    /**
     * 根据组织ID集合获取党员列表
     *
     * @param query
     * @return
     */
    List<PartyMember> findByOrgIdsPage(PartyMemberQuery query);

    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberVO
     * @return
     */
    List<PartyMemberVO> findByConditionPage2(PartyMemberParamsVO partyMemberVO);

    /**
     * 查询停用党员信息分页
     *
     * @param partyMemberVO
     * @return
     */
    List<PartyMemberVO> findByConditionPage3(PartyMemberParamsVO partyMemberVO);

    /**
     * 获取退伍党员信息分页
     *
     * @param partyMemberExServiceman
     * @return
     */
    List<PartyMemberExServiceman> findExServicemanPage(PartyMemberExServicemanVO partyMemberExServiceman);

    /**
     * 查询所有PartyMember
     *
     * @return
     */
    List<PartyMemberExportVO> findAll(PartyMemberParamsVO partyMember);

    /**
     * 导出停用党员
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
    PartyMember findById(@Param(value = "id") Long id);

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
    void add(PartyMember partyMember);

    /**
     * TODO
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
    void update(PartyMember partyMember);

    /**
     * 党员转移更改状态
     *
     * @param id
     * @param orgId
     */
    void updateByTransfer(@Param(value = "id") Long id, @Param(value = "orgId") String orgId);

    /**
     * 删除PartyMember
     *
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 删除PartyMember
     *
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    /**
     * 根据组织ID查询已转出的党员记录
     *
     * @param orgId
     * @return
     */
    List<PartyMember> findRollOutsByOrgId(@Param(value = "orgId") Long orgId);

    /**
     * 根据组织ID查询停用的党员记录
     *
     * @param orgId
     * @return
     */
    List<PartyMember> findDisableMembersByOrgId(@Param(value = "orgId") Long orgId);

    /**
     * 根据身份证获取党员信息
     *
     * @param idCard
     * @return
     */
    PartyMember findByIdCard(@Param(value = "idCard") String idCard);

    /**
     * 根据身份证集合获取多个党员信息
     *
     * @param idCards
     * @return
     */
    List<PartyMember> findByIdCards(List idCards);

    /**
     * 导出退伍军人
     *
     * @param exServicemanParamVO
     * @return
     */
    List<PartyMemberExportVO> findExServiceman(ExServicemanParamVO exServicemanParamVO);

    Integer countPartyMemberByNameAndIdCard(@Param(value = "name") String name, @Param(value = "idCard") String idCard);

    PartyMember findByIdCardAndStatus(PartyMember toQuery);
}
