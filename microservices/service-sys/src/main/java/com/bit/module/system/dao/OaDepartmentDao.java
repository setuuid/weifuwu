package com.bit.module.system.dao;

import java.util.List;

import com.bit.module.system.bean.Identity;
import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.vo.OaDepartmentVO;
import org.apache.ibatis.annotations.Param;

/**
 * OaDepartment管理的Dao
 * @author 
 *
 */
public interface OaDepartmentDao {
	/**
	 * 根据条件查询OaDepartment
	 * @param oaDepartmentVO
	 * @return
	 */
	public List<OaDepartment> findByConditionPage(OaDepartmentVO oaDepartmentVO);
	/**
	 * 查询所有OaDepartment
	 * @return
	 */
	public List<OaDepartment> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个OaDepartment
	 * @param id	 	 
	 * @return
	 */
	public OaDepartment findById(@Param(value = "id") Integer id);
	/**
	 * 批量保存OaDepartment
	 * @param oaDepartments
	 */
	public void batchAdd(List<OaDepartment> oaDepartments);
	/**
	 * 保存OaDepartment
	 * @param oaDepartment
	 */
	public void add(OaDepartment oaDepartment);
	/**
	 * 批量更新OaDepartment
	 * @param oaDepartments
	 */
	public void batchUpdate(List<OaDepartment> oaDepartments);
	/**
	 * 更新OaDepartment
	 * @param oaDepartment
	 */
	public void update(OaDepartment oaDepartment);
	/**
	 * 删除OaDepartment
	 * @param oaDepartments
	 */
	public void batchDelete(List<Long> ids);
	/**
	 * 删除OaDepartment
	 * @param id
	 */
	public void delete(@Param(value = "id") Integer id);

	/**
	 * 根据用户userId 查询
	 * @param userId
	 * @return
	 */
    List<OaDepartment> findByUserId(@Param(value = "userId") Long userId);
}
