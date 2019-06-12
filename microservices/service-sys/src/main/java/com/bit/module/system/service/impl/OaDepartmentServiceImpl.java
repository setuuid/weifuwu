package com.bit.module.system.service.impl;

import java.util.List;

import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.dao.OaDepartmentDao;
import com.bit.module.system.service.OaDepartmentService;
import com.bit.module.system.vo.OaDepartmentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bit.base.vo.BaseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bit.base.service.BaseService;

/**
 * OaDepartment的Service实现类
 * @author codeGenerator
 *
 */
@Service("oaDepartmentService")
public class OaDepartmentServiceImpl extends BaseService implements OaDepartmentService {
	
	private static final Logger logger = LoggerFactory.getLogger(OaDepartmentServiceImpl.class);
	
	@Autowired
	private OaDepartmentDao oaDepartmentDao;
	
	/**
	 * 根据条件查询OaDepartment
	 * @param oaDepartmentVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(OaDepartmentVO oaDepartmentVO){
		PageHelper.startPage(oaDepartmentVO.getPageNum(), oaDepartmentVO.getPageSize());
		List<OaDepartment> list = oaDepartmentDao.findByConditionPage(oaDepartmentVO);
		PageInfo<OaDepartment> pageInfo = new PageInfo<OaDepartment>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有OaDepartment
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<OaDepartment> findAll(String sorter){
		return oaDepartmentDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个OaDepartment
	 * @param id
	 * @return
	 */
	@Override
	public OaDepartment findById(Integer id){
		return oaDepartmentDao.findById(id);
	}
	
	/**
	 * 批量保存OaDepartment
	 * @param oaDepartments
	 */
	@Override
	public void batchAdd(List<OaDepartment> oaDepartments){
		oaDepartmentDao.batchAdd(oaDepartments);
	}
	/**
	 * 保存OaDepartment
	 * @param oaDepartment
	 */
	@Override
	public void add(OaDepartment oaDepartment){
		oaDepartmentDao.add(oaDepartment);
	}
	/**
	 * 批量更新OaDepartment
	 * @param oaDepartments
	 */
	@Override
	public void batchUpdate(List<OaDepartment> oaDepartments){
		oaDepartmentDao.batchUpdate(oaDepartments);
	}
	/**
	 * 更新OaDepartment
	 * @param oaDepartment
	 */
	@Override
	public void update(OaDepartment oaDepartment){
		oaDepartmentDao.update(oaDepartment);
	}
	/**
	 * 删除OaDepartment
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		oaDepartmentDao.batchDelete(ids);
	}
	/**
	 * 删除OaDepartment
	 * @param id
	 */
	@Override
	public void delete(Integer id){
		oaDepartmentDao.delete(id);
	}
}
