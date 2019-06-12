package com.bit.module.pb.dao;

import com.bit.module.pb.bean.PartyMemberApproval;
import com.bit.module.pb.vo.PartyMemberApprovalVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PartyMemberApproval管理的Dao
 * @author 
 *
 */
@Repository
public interface PartyMemberApprovalDao {
	/**
	 * 根据条件查询PartyMemberApproval
	 * @param partyMemberApprovalVO
	 * @return
	 */
	List<PartyMemberApproval> findByConditionPage(PartyMemberApprovalVO partyMemberApprovalVO);
	/**
	 * 查询所有PartyMemberApproval
	 * @return
	 */
	List<PartyMemberApproval> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个PartyMemberApproval
	 * @param id	 	 
	 * @return
	 */
	PartyMemberApproval findById(@Param(value = "id") Long id);
	/**
	 * 批量保存PartyMemberApproval
	 * @param partyMemberApprovals
	 */
	void batchAdd(List<PartyMemberApproval> partyMemberApprovals);
	/**
	 * 保存PartyMemberApproval
	 * @param partyMemberApproval
	 */
	void add(PartyMemberApproval partyMemberApproval);
	/**
	 * 批量更新PartyMemberApproval
	 * @param partyMemberApprovals
	 */
	void batchUpdate(List<PartyMemberApproval> partyMemberApprovals);
	/**
	 * 更新PartyMemberApproval
	 * @param partyMemberApproval
	 */
	void update(PartyMemberApproval partyMemberApproval);

	/**
	 * 更改状态
	 * @param id
	 * @param status
	 */
	void updateByStatus(@Param(value = "id") Long id, @Param(value = "status") Integer status);
	/**
	 * 删除PartyMemberApproval
	 * @param ids
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除PartyMemberApproval
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 查询是否提交申请
	 * @param memberId
	 * @param type
	 * @return
	 */
	PartyMemberApproval findRecord(@Param(value = "memberId") Long memberId, @Param(value = "type") Integer type);

	/**
	 * 获取停用原因
	 * @param memberId
	 * @return
	 */
	PartyMemberApproval findOutreason(@Param(value = "memberId") Long memberId, @Param(value = "type") Integer type);
}
