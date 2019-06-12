package com.bit.module.pb.dao;

import com.bit.module.pb.bean.RelationshipTransfer;
import com.bit.module.pb.vo.PartyMemberParamsVO;
import com.bit.module.pb.vo.RelationshipTransferParamsVO;
import com.bit.module.pb.vo.RelationshipTransferVO;
import com.bit.module.pb.vo.TransferPartyMemberExportVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RelationshipTransfer管理的Dao
 *
 * @author
 */
@Repository
public interface RelationshipTransferDao {
    /**
     * 根据条件查询RelationshipTransfer
     *
     * @param relationshipTransferVO
     * @return
     */
    List<RelationshipTransfer> findByConditionPage(RelationshipTransferVO relationshipTransferVO);

    /**
     * 党员内转
     *
     * @param relationshipTransferVO
     * @return
     */
    List<RelationshipTransfer> findByOutListPage(RelationshipTransferVO relationshipTransferVO);

    /**
     * 党员接收
     *
     * @param relationshipTransferVO
     * @return
     */
    List<RelationshipTransfer> findByReceptionPage(RelationshipTransferVO relationshipTransferVO);

    /**
     * 获取党员转移记录
     *
     * @param memberId
     * @param idCard
     * @return
     */
    List<RelationshipTransfer> findAll(@Param(value = "memberId") Long memberId, @Param(value = "idCard") String idCard, @Param(value = "status") Integer status);

    /**
     * 镇内互转的分页
     *
     * @param paramsVO
     * @return
     */
    List<RelationshipTransfer> findByBetweenPage(RelationshipTransferParamsVO paramsVO);

    /**
     * 镇内转镇外
     *
     * @param paramsVO
     * @return
     */
    List<RelationshipTransfer> findByOutPage(RelationshipTransferParamsVO paramsVO);

    /**
     * 镇外转镇内
     *
     * @param paramsVO
     * @return
     */
    List<RelationshipTransfer> findByInPage(RelationshipTransferParamsVO paramsVO);

    /**
     * 根据条件查询RelationshipTransfer
     *
     * @param partyMemberVO
     * @return
     */
    List<RelationshipTransfer> findByConditionPage3(PartyMemberParamsVO partyMemberVO);

    /**
     * 查询所有RelationshipTransfer
     *
     * @return
     */
    List<RelationshipTransfer> findAll(@Param(value = "sorter") String sorter);

    /**
     * 通过主键查询单个RelationshipTransfer
     *
     * @param id
     * @return
     */
    RelationshipTransfer findById(@Param(value = "id") Long id);

    /**
     * 根据党员ID获取
     *
     * @param memberId
     * @return
     */
    RelationshipTransfer findRecord(@Param(value = "memberId") Long memberId, @Param(value = "status") Integer status);

    /**
     * 批量保存RelationshipTransfer
     *
     * @param relationshipTransfers
     */
    void batchAdd(List<RelationshipTransfer> relationshipTransfers);

    /**
     * 保存RelationshipTransfer
     *
     * @param relationshipTransfer
     */
    void add(RelationshipTransfer relationshipTransfer);

    /**
     * 批量更新RelationshipTransfer
     *
     * @param relationshipTransfers
     */
    void batchUpdate(List<RelationshipTransfer> relationshipTransfers);

    /**
     * 更新RelationshipTransfer
     *
     * @param relationshipTransfer
     */
    void update(RelationshipTransfer relationshipTransfer);

    /**
     * 删除RelationshipTransfer
     *
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 删除RelationshipTransfer
     *
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    /**
     * 导出转出党员列表
     *
     * @param relationshipTransfer
     * @return
     */
    List<TransferPartyMemberExportVO> findTransferPartyMember(RelationshipTransfer relationshipTransfer);
}
