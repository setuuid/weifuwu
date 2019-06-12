package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.PartyMemberSummary;
import com.bit.module.pb.bean.RelationshipTransfer;
import com.bit.module.pb.vo.PartyMemberParamsVO;
import com.bit.module.pb.vo.RelationshipTransferParamsVO;
import com.bit.module.pb.vo.RelationshipTransferVO;
import com.bit.module.pb.vo.TransferPartyMemberExportVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * RelationshipTransfer的Service
 *
 * @author codeGenerator
 */
public interface RelationshipTransferService {
    /**
     * 根据条件查询RelationshipTransfer
     *
     * @param relationshipTransferVO
     * @return
     */
    BaseVo findByConditionPage(RelationshipTransferVO relationshipTransferVO);

    /**
     * 党员内转
     *
     * @param relationshipTransferVO
     * @return
     */
    PageInfo findByOutListPage(RelationshipTransferVO relationshipTransferVO);

    /**
     * 党员接收
     *
     * @param relationshipTransferVO
     * @return
     */
    PageInfo findByReceptionPage(RelationshipTransferVO relationshipTransferVO);

    /**
     * 根据条件查询RelationshipTransfer
     *
     * @param partyMemberVO
     * @return
     */
    PageInfo findByConditionPage3(PartyMemberParamsVO partyMemberVO);

    /**
     * 根据条件查询RelationshipTransfer
     *
     * @param paramsVO
     * @return
     */
    PageInfo findByTransferPage(RelationshipTransferParamsVO paramsVO);

    /**
     * 查询所有RelationshipTransfer
     *
     * @param memberId
     * @return
     */
    List<RelationshipTransfer> findAll(Long memberId, String idCard, Integer status);

    /**
     * 通过主键查询单个RelationshipTransfer
     *
     * @param id
     * @return
     */
    RelationshipTransfer findById(Long id);

    /**
     * 根据党员获取转移记录
     *
     * @param memberId
     * @return
     */
    List<RelationshipTransfer> findByMemberId(Long memberId);

    /**
     * 批量保存RelationshipTransfer
     *
     * @param relationshipTransfers
     */
    void batchAdd(List<RelationshipTransfer> relationshipTransfers);

    /**
     * 保存RelationshipTransfer
     *
     * @param relationshipTransferVO
     */
    void add(RelationshipTransferVO relationshipTransferVO);

    /**
     * 接收镇外党员
     *
     * @param partyMemberSummary
     */
    void reception(PartyMemberSummary partyMemberSummary);

    /**
     * 批量更新RelationshipTransfer
     *
     * @param relationshipTransfers
     */
    void batchUpdate(List<RelationshipTransfer> relationshipTransfers);

    /**
     * 更新RelationshipTransfer
     *
     * @param relationshipTransferVO
     */
    void update(RelationshipTransferVO relationshipTransferVO);

    /**
     * 更新镇外转移
     *
     * @param partyMemberSummary
     */
    void updateReception(PartyMemberSummary partyMemberSummary);

    /**
     * 审核流程
     *
     * @param id
     * @param status
     */
    void updateFlow(Long id, Integer status, String reason);

    /**
     * 删除RelationshipTransfer
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 批量删除RelationshipTransfer
     *
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 导出转出党员
     *
     * @param relationshipTransfer
     * @return
     */
    List<TransferPartyMemberExportVO> findTransferPartyMember(RelationshipTransfer relationshipTransfer);
}
