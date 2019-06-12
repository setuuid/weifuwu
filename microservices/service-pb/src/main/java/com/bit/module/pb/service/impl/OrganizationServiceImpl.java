package com.bit.module.pb.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.dao.OrganizationDao;
import com.bit.module.pb.service.OrganizationService;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.OrganizationVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Organization的Service实现类
 * @author codeGenerator
 *
 */
@Service("organizationService")
public class OrganizationServiceImpl extends BaseService implements OrganizationService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	
	@Autowired
	private OrganizationDao organizationDao;

	/**
	 * 根据条件查询Organization
	 * @param organizationVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(OrganizationVO organizationVO){
		PageHelper.startPage(organizationVO.getPageNum(), organizationVO.getPageSize());
		List<Organization> list = organizationDao.findByConditionPage(organizationVO);
		PageInfo<Organization> pageInfo = new PageInfo<Organization>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有Organization
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<Organization> findAll(String sorter){
		return organizationDao.findAll(sorter);
	}

	@Override
	public Organization findDirectSuperiorById(String orgId) {
		return organizationDao.findDirectSuperiorById(orgId, true);
	}

	/**
	 * 通过主键查询单个Organization
	 * @param id
	 * @return
	 */
	@Override
	public Organization findById(String id){
		return organizationDao.findById(id);
	}
	
	/**
	 * 批量保存Organization
	 * @param organizations
	 */
	@Override
	public void batchAdd(List<Organization> organizations){
		organizationDao.batchAdd(organizations);
	}
	/**
	 * 保存Organization
	 * @param organization
	 */
	@Override
	public void add(Organization organization){
		organizationDao.add(organization);
	}
	/**
	 * 批量更新Organization
	 * @param organizations
	 */
	@Override
	public void batchUpdate(List<Organization> organizations){
		organizationDao.batchUpdate(organizations);
	}
	/**
	 * 更新Organization
	 * @param organization
	 */
	@Override
	public void update(Organization organization){
		organizationDao.update(organization);
	}
	/**
	 * 删除Organization
	 * @param ids
	 */
	@Override
	public void batchDelete(List<String> ids){
		organizationDao.batchDelete(ids);
	}

	@Override
	public List<OrganizationInfoVO> getDirectSubOrganization(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            List<Organization> all = organizationDao.findAll(null);
            return all.stream().map(org -> {
                OrganizationInfoVO organizationInfoVO = new OrganizationInfoVO();
                organizationInfoVO.setId(org.getId());
                organizationInfoVO.setName(org.getName());
                return organizationInfoVO;
            }).collect(Collectors.toList());
        }
		return organizationDao.findDirectSubordinatesInfoById(orgId);
	}

    /**
	 * 删除Organization
	 * @param id
	 */
	@Override
	public void delete(String id){
		organizationDao.delete(id);
	}
}
