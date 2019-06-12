package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.OrganizationVO;

import java.util.List;
/**
 * Organization的Service
 * @author codeGenerator
 */
public interface OrganizationService {
	/**
	 * 根据条件查询Organization
	 * @param organizationVO
	 * @return
	 */
	BaseVo findByConditionPage(OrganizationVO organizationVO);
	/**
	 * 查询所有Organization
	 * @param sorter 排序字符串
	 * @return
	 */
	List<Organization> findAll(String sorter);

	Organization findDirectSuperiorById(String orgId);
	/**
	 * 通过主键查询单个Organization
	 * @param id
	 * @return
	 */
	Organization findById(String id);

	/**
	 * 批量保存Organization
	 * @param organizations
	 */
	void batchAdd(List<Organization> organizations);
	/**
	 * 保存Organization
	 * @param organization
	 */
	void add(Organization organization);
	/**
	 * 批量更新Organization
	 * @param organizations
	 */
	void batchUpdate(List<Organization> organizations);
	/**
	 * 更新Organization
	 * @param organization
	 */
	void update(Organization organization);
	/**
	 * 删除Organization
	 * @param id
	 */
	void delete(String id);
	/**
	 * 批量删除Organization
	 * @param ids
	 */
	void batchDelete(List<String> ids);

	/**
	 * 获取下级组织
	 * @param orgId
	 * @return
	 */
    List<OrganizationInfoVO> getDirectSubOrganization(String orgId);
}
