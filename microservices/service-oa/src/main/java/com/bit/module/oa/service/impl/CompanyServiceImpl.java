package com.bit.module.oa.service.impl;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bit.base.exception.BusinessException;
import com.bit.module.oa.enums.CompanyConditionEnum;
import com.bit.module.oa.enums.CompanyStatusEnum;
import com.bit.module.oa.vo.company.CompanyExportVO;
import com.bit.module.oa.vo.company.CompanyPageVO;
import com.bit.module.oa.vo.company.SimpleCompanyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bit.base.vo.BaseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bit.module.oa.bean.Company;
import com.bit.module.oa.vo.company.CompanyVO;
import com.bit.module.oa.dao.CompanyDao;
import com.bit.module.oa.service.CompanyService;
import com.bit.base.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Company的Service实现类
 * @author codeGenerator
 *
 */
@Service("companyService")
@Transactional
public class CompanyServiceImpl extends BaseService implements CompanyService{

	private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	private CompanyDao companyDao;

	/**
	 * 根据条件查询Company
	 * @param companyVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(CompanyVO companyVO){
		PageHelper.startPage(companyVO.getPageNum(), companyVO.getPageSize());
		List<CompanyPageVO> list = companyDao.findByConditionPage(companyVO);
		PageInfo<CompanyPageVO> pageInfo = new PageInfo<>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	@Override
	public List<CompanyExportVO> findExportCompany(String nature) {
		List<CompanyPageVO> list = companyDao.findByCondition(nature);

		return list.stream().map(source -> {
			CompanyExportVO target = new CompanyExportVO();
			BeanUtils.copyProperties(source, target);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			target.setCondition(CompanyConditionEnum.getByKey(source.getCondition()).getDescription());
			String startTime = sdf.format(source.getServiceStartTime());
			String endTime = sdf.format(source.getServiceEndTime());
			String date = startTime + "--" + endTime;
			target.setServiceTime(date);
			return target;
		}).collect(Collectors.toList());
	}
	/**
	 * 查询所有Company
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<SimpleCompanyVO> findAll(String sorter){
		return companyDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个Company
	 * @param id
	 * @return
	 */
	@Override
	public Company findById(Long id){
		return companyDao.findById(id);
	}

	/**
	 * 批量保存Company
	 * @param companys
	 */
	@Override
	public void batchAdd(List<Company> companys){
		companyDao.batchAdd(companys);
	}
	/**
	 * 保存Company
	 * @param company
	 */
	@Override
	public void add(Company company){
		company.setStatus(CompanyStatusEnum.DISABLE.getKey());
		companyDao.add(company);
	}
	/**
	 * 批量更新Company
	 * @param companys
	 */
	@Override
	public void batchUpdate(List<Company> companys){
		companyDao.batchUpdate(companys);
	}
	/**
	 * 更新Company
	 * @param company
	 */
	@Override
	public void update(Company company){
		Company toUpdate = companyDao.findById(company.getId());
		if (toUpdate == null) {
			logger.error("三方公司不存在, {}", company);
			throw new BusinessException("三方公司不存在");
		}
		// TODO 找政府经办人id
		companyDao.update(company);
	}
	/**
	 * 删除Company
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		companyDao.batchDelete(ids);
	}

	@Override
	public void convertStatus(Long id, Integer status) {
		Company toConvertStatus = companyDao.findById(id);
		if (toConvertStatus == null) {
			logger.error("三方公司{}不存在", id);
			throw new BusinessException("三方公司不存在");
		}
		if (toConvertStatus.getStatus().equals(status)) {
			logger.error("三方公司的状态已经是{}", status);
			throw new BusinessException("三方公司状态不匹配");
		}
		companyDao.updateStatus(id, status);
	}

	/**
	 * 删除Company
	 * @param id
	 */
	@Override
	public void delete(Long id){
		companyDao.delete(id);
	}
}
