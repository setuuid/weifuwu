package com.bit.module.pb.dao;

import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.OrganizationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Organization管理的Dao
 * @author 
 *
 */
@Repository
public interface OrganizationDao {
	/**
	 * 根据条件查询Organization
	 * @param organizationVO
	 * @return
	 */
	List<Organization> findByConditionPage(OrganizationVO organizationVO);

	/**
	 * 查询所有Organization
	 * @return
	 */
	List<Organization> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个Organization
	 * @param id	 	 
	 * @return
	 */
	Organization findById(@Param(value = "id") String id);
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
     * @param ids
     */
	void batchDelete(List<String> ids);
	/**
	 * 删除Organization
	 * @param id
	 */
	void delete(@Param(value = "id") String id);

	/**
	 * 查询所有基层单位
	 * @return
	 */
	List<Organization> findAllGrassRootsUnits();

	/**
	 * 根据id查询组织的下级
	 * @return
	 */
	List<Organization> findSubordinatesById(@Param(value = "id") String id, @Param(value = "includeItself") Boolean includeItself);

	/**
	 * 根据id查询组织的直接下级
	 * @param id
	 * @return
	 */
	List<OrganizationInfoVO> findDirectSubordinatesInfoById(@Param(value = "id") String id);

	/**
	 * 根据id查询直属上级组织
	 * @param id
	 * @param isApprovalAuz
	 * @return
	 */
	Organization findDirectSuperiorById(@Param(value = "id") String id, @Param("isApprovalAuz") Boolean isApprovalAuz);

	/**
	 * 在组织下添加子组织
	 * @param organization
	 */
	void nestedAdd(Organization organization);

	/**
	 * 级联删除
	 * @param id
	 */
	void cascadeDelete(@Param(value = "id") String id);
}
