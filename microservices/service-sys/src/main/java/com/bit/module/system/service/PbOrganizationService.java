package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.PbOrganization;
import com.bit.module.system.vo.PbOrganizationVO;

import java.util.List;
/**
 * PbOrganization的Service
 * @author liqi
 */
public interface PbOrganizationService {
	/**
	 * 根据条件查询PbOrganization
	 * @param pbOrganizationVO
	 * @return
	 */
	BaseVo findByConditionPage(PbOrganizationVO pbOrganizationVO);

	/**
	 * 查询所有PbOrganization
	 * @param sorter 排序字符串
	 * @return
	 */
	List<PbOrganization> findAll(String sorter);

	/**
	 * 通过主键查询单个PbOrganization
	 * @param strId
	 * @return
	 */
	BaseVo findById(String strId);

	/**
	 * 保存PbOrganization
	 * @param pbOrganization
	 */
	void add(PbOrganization pbOrganization);

	/**
	 * 更新PbOrganization
	 * @param pbOrganization
	 */
	void update(PbOrganization pbOrganization);

	/**
	 * 删除PbOrganization
	 * @param strId
	 */
	void delete(String strId);

	/**
	 * 批量删除PbOrganization
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 查询党政组织如果有下级组织不能删除  如果被用户用了也不能删除
	 * @param id
	 * @return
	 */
	BaseVo checkNexusByPbId(String id);

	/**
	 * 查询
	 * @param strId
	 * @return
	 */
	BaseVo findChildListByPid(String strId);

	/**
	 * 根据参数查询
	 * @param pbOrganization
	 * @return
	 */
	List<PbOrganization> findAllByParam(PbOrganization pbOrganization);

	/**
	 * 查询树
	 * @param pbOrganization
	 * @return
	 */
	List<PbOrganization> findTreeByParam(PbOrganization pbOrganization);

	/**
	 * 校验组织编码
	 * @param pbOrganization
	 * @return
	 */
    BaseVo checkPcode(PbOrganization pbOrganization);
}
