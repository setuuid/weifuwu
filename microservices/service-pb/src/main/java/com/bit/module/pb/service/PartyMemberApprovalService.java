package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.PartyMemberApproval;
import com.bit.module.pb.bean.PartyMemberSummary;
import com.bit.module.pb.vo.PartyMemberApprovalVO;

import java.util.List;

/**
 * PartyMemberApproval的Service
 *
 * @author codeGenerator
 */
public interface PartyMemberApprovalService {
    /**
     * 根据条件查询PartyMemberApproval
     *
     * @param partyMemberApprovalVO
     * @return
     */
    BaseVo findByConditionPage(PartyMemberApprovalVO partyMemberApprovalVO);

    /**
     * 查询所有PartyMemberApproval
     *
     * @param sorter 排序字符串
     * @return
     */
    List<PartyMemberApproval> findAll(String sorter);

    /**
     * 通过主键查询单个PartyMemberApproval
     *
     * @param id
     * @return
     */
    PartyMemberApproval findById(Long id);

    /**
     * 批量保存PartyMemberApproval
     *
     * @param partyMemberApprovals
     */
    @Deprecated
    void batchAdd(List<PartyMemberApproval> partyMemberApprovals);

    /**
     * 保存PartyMemberApproval
     *
     * @param partyMemberSummary
     */
    void add(PartyMemberSummary partyMemberSummary);

    /**
     * 提交党员申请
     * @param id
     */
    void submit(Long id, Long userId);

    /**
     * 退回申请
     * @param id
     */
    void sendBack(Long id, String reason, Long userId);

    /**
     * 通过审核
     * @param id
     */
    PartyMemberApproval pass(Long id, Long userId);

    /**
     * 启用党员
     *
     * @param partyMemberApproval
     */
    PartyMemberApproval enabled(PartyMemberApproval partyMemberApproval);

    /**
     * 停用党员
     *
     * @param partyMemberApproval
     */
    PartyMemberApproval disable(PartyMemberApproval partyMemberApproval);

    /**
     * 批量更新PartyMemberApproval
     *
     * @param partyMemberApprovals
     */
    @Deprecated
    void batchUpdate(List<PartyMemberApproval> partyMemberApprovals);

    /**
     * 更新PartyMemberApproval
     *
     * @param partyMemberSummary
     */
    void update(PartyMemberSummary partyMemberSummary);

    /**
     * 更改开关状态的审批信息
     * @param partyMemberApprovalVO
     */
    void updateBySwitch(PartyMemberApprovalVO partyMemberApprovalVO);

    /**
     * 更改数据状态
     * @param id
     * @param status
     */
    void updateByStatus(Long id, Integer status);

    /**
     * 删除PartyMemberApproval
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 批量删除PartyMemberApproval
     *
     * @param ids
     */
    @Deprecated
    void batchDelete(List<Long> ids);

    /**
     * 查询审核记录
     * @param memberId
     * @param type
     * @param status
     * @return
     */
    Boolean findRecord(Long memberId, Integer type, Integer status);

    /**
     * 获取停用原因
     * @param memberId
     * @return
     */
    PartyMemberApproval findOutreason(Long memberId);
}
