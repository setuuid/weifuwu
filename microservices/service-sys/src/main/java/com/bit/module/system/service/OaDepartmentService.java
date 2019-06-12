package com.bit.module.system.service;

import java.util.List;

import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.vo.OaDepartmentVO;
import com.bit.base.vo.BaseVo;
/**
 * OaDepartment的Service
 * @author codeGenerator
 */
public interface OaDepartmentService {
	/**
	 * 根据条件查询OaDepartment
	 * @param oaDepartmentVO
	 * @return
	 */
	BaseVo findByConditionPage(OaDepartmentVO oaDepartmentVO);
	/**
	 * 查询所有OaDepartment
	 * @param sorter 排序字符串
	 * @return
	 */
	List<OaDepartment> findAll(String sorter);
	/**
	 * 通过主键查询单个OaDepartment
	 * @param id
	 * @return
	 */
	OaDepartment findById(Integer id);

	/**
	 * 批量保存OaDepartment
	 * @param oaDepartments
	 */
	void batchAdd(List<OaDepartment> oaDepartments);
	/**
	 * 保存OaDepartment
	 * @param oaDepartment
	 */
	void add(OaDepartment oaDepartment);
	/**
	 * 批量更新OaDepartment
	 * @param oaDepartments
	 */
	void batchUpdate(List<OaDepartment> oaDepartments);
	/**
	 * 更新OaDepartment
	 * @param oaDepartment
	 */
	void update(OaDepartment oaDepartment);
	/**
	 * 删除OaDepartment
	 * @param id
	 */
	void delete(Integer id);
	/**
	 * 批量删除OaDepartment
	 * @param ids
	 */
	void batchDelete(List<Long> ids);
}
