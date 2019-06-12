package com.bit.module.oa.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Company;
import com.bit.module.oa.bean.VehicleApplication;
import com.bit.module.oa.bean.VehicleLog;
import com.bit.module.oa.dao.*;
import com.bit.module.oa.enums.*;
import com.bit.module.oa.service.VehicleApplicationService;
import com.bit.module.oa.vo.driver.SimpleDriverVO;
import com.bit.module.oa.vo.vehicle.SimpleVehicleVO;
import com.bit.module.oa.vo.vehicleApplication.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * VehicleApplication的Service实现类
 * @author codeGenerator
 *
 */
@Service("vehicleApplicationService")
@Transactional
public class VehicleApplicationServiceImpl extends BaseService implements VehicleApplicationService{
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleApplicationServiceImpl.class);

	private static final Integer DATA_SIZE = 100;
	
	@Autowired
	private VehicleApplicationDao vehicleApplicationDao;

	@Autowired
	private VehicleLogDao vehicleLogDao;

	@Autowired
	private VehicleDao vehicleDao;

	@Autowired
	private DriverDao driverDao;

	@Autowired
	private CompanyDao companyDao;

	/**
	 * 根据条件查询VehicleApplication
	 * @param vehicleApplicationVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(VehicleApplicationVO vehicleApplicationVO){
		PageHelper.startPage(vehicleApplicationVO.getPageNum(), vehicleApplicationVO.getPageSize());
		List<VehicleApplication> list = vehicleApplicationDao.findByConditionPage(vehicleApplicationVO);
		PageInfo<VehicleApplication> pageInfo = new PageInfo<VehicleApplication>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	@Override
	public BaseVo findMyVehicleApplicationPage(VehicleApplicationQO vehicleApplicationQO) {
		PageHelper.startPage(vehicleApplicationQO.getPageNum(), vehicleApplicationQO.getPageSize());
		List<MyVehicleApplication> list = vehicleApplicationDao.findMyVehicleApplication(vehicleApplicationQO);
		PageInfo<MyVehicleApplication> pageInfo = new PageInfo<>(list);
		return new BaseVo(pageInfo);
	}

	@Override
	public List<MyVehicleApplicationExportVO> findExportMyVehicleUsing(VehicleApplicationQO vehicleApplicationQO) {
		List<VehicleApplicationDetail> list = vehicleApplicationDao.findExportDataByCondition(vehicleApplicationQO);
		return list.stream().map(source -> {
			MyVehicleApplicationExportVO target = new MyVehicleApplicationExportVO();
			BeanUtils.copyProperties(source, target);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			target.setNature(VehicleApplicationNatureEnum.getByKey(source.getNature()).getDescription());
			target.setUsage(VehicleApplicationUsageEnum.getByKey(source.getUsage()).getDescription());
			String startTime = sdf.format(source.getPlanStartTime());
			String endTime = sdf.format(source.getPlanEndTime());
			String date = startTime + "--" + endTime;
			target.setPlanTime(date);
			return target;
		}).collect(Collectors.toList());
	}

	@Override
	public BaseVo findLedgerApplicationListPage(VehicleApplicationQO vehicleApplicationQO) {
		PageHelper.startPage(vehicleApplicationQO.getPageNum(), vehicleApplicationQO.getPageSize());
		List<ManagerVehicleApplicationVO> list = vehicleApplicationDao.findLedgerVehicleApplication(vehicleApplicationQO);
		PageInfo<ManagerVehicleApplicationVO> pageInfo = new PageInfo<>(list);
		return new BaseVo(pageInfo);
	}

	@Override
	public BaseVo findHandleApplicationListPage(VehicleApplicationQO vehicleApplicationQO) {
		PageHelper.startPage(vehicleApplicationQO.getPageNum(), vehicleApplicationQO.getPageSize());
		List<ManagerVehicleApplicationVO> list = vehicleApplicationDao.findHandleVehicleApplication(vehicleApplicationQO);
		PageInfo<ManagerVehicleApplicationVO> pageInfo = new PageInfo<>(list);
		return new BaseVo(pageInfo);
	}

	@Override
	public List<HandleVehicleApplicationExportVO> findHandleApplicationExport(
			VehicleApplicationQO vehicleApplicationQO) {
		List<VehicleApplicationDetail> list = vehicleApplicationDao.findExportDataByCondition(vehicleApplicationQO);
		return getExportVehicleApplicationData(list);
	}


	@Override
	public List<HandleVehicleApplicationExportVO> findLedgerApplicationExport(
			VehicleApplicationQO vehicleApplicationQO) {
		List<VehicleApplicationDetail> list = vehicleApplicationDao.findLedgerExportDataByCondition(vehicleApplicationQO);
		return getExportVehicleApplicationData(list);
	}

	private List<HandleVehicleApplicationExportVO> getExportVehicleApplicationData(
			List<VehicleApplicationDetail> list) {
		return list.stream().map(source -> {
			HandleVehicleApplicationExportVO target = new HandleVehicleApplicationExportVO();
			BeanUtils.copyProperties(source, target);
			// todo 暂定null
			target.setAssignerName(null);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			target.setNature(VehicleApplicationNatureEnum.getByKey(source.getNature()).getDescription());
			target.setUsage(VehicleApplicationUsageEnum.getByKey(source.getUsage()).getDescription());
			String planStartTime = sdf.format(source.getPlanStartTime());
			String planEndTime = sdf.format(source.getPlanEndTime());
			String realStartTime = StringUtils.isEmpty(source.getStartTime()) ? null : sdf
					.format(source.getStartTime());
			String realEndTime = StringUtils.isEmpty(source.getEndTime()) ? null : sdf.format(source.getEndTime());
			String planTime = planStartTime + "--" + planEndTime;
			String realTime = StringUtils.isEmpty(realStartTime) ? null : realStartTime + "--" + realEndTime;
			target.setPlanTime(planTime);
			target.setRealTime(realTime);
			return target;
		}).collect(Collectors.toList());
	}

	/**
	 * 查询所有VehicleApplication
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<VehicleApplication> findAll(String sorter){
		return vehicleApplicationDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个VehicleApplication
	 * @param id
	 * @return
	 */
	@Override
	public VehicleApplication findById(Long id){
		return vehicleApplicationDao.findById(id);
	}
	
	/**
	 * 批量保存VehicleApplication
	 * @param vehicleApplications
	 */
	@Override
	public void batchAdd(List<VehicleApplication> vehicleApplications) {
		vehicleApplicationDao.batchAdd(vehicleApplications);
	}
	/**
	 * 保存VehicleApplication
	 * @param vehicleApplication
	 */
	@Override
	public void apply(VehicleApplication vehicleApplication){
		VehicleApplicationUsageEnum usageEnum = VehicleApplicationUsageEnum.getByKey(vehicleApplication.getUsage());
		if (usageEnum == null) {
			logger.error("用车用途不合法 : {}", vehicleApplication.getUsage());
			throw new BusinessException("用车用途不合法");
		}
		vehicleApplication.setApplyNo(generateApplyNo(usageEnum));
		vehicleApplication.setApplyTime(new Date());
		vehicleApplication.setStatus(VehicleApplicationStatusEnum.DRAFT.getKey());
		vehicleApplicationDao.apply(vehicleApplication);
	}

	/**
	 * 批量更新VehicleApplication
	 * @param vehicleApplications
	 */
	@Override
	public void batchUpdate(List<VehicleApplication> vehicleApplications){
		vehicleApplicationDao.batchUpdate(vehicleApplications);
	}
	/**
	 * 更新VehicleApplication
	 * @param vehicleApplication
	 */
	@Override
	public void update(VehicleApplication vehicleApplication){
		vehicleApplicationDao.update(vehicleApplication);
	}
	/**
	 * 删除VehicleApplication
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		vehicleApplicationDao.batchDelete(ids);
	}

	@Override
	public VehicleApplicationDetail findVehicleApplicationDetail(VehicleApplication toQuery) {
		VehicleApplicationDetail vehicleApplication = vehicleApplicationDao.findById(toQuery.getId());
		if (toQuery.getUserId() != null && !vehicleApplication.getUserId().equals(toQuery.getUserId())) {
			logger.error("该用户无权限查询此用车记录{}", vehicleApplication);
			throw new BusinessException("该用户无权限查询此用车记录");
		}
		return vehicleApplication;
	}

	@Override
	public void reject(VehicleApplication vehicleApplication) {
		VehicleApplication toCheck = checkApplicationStatus(vehicleApplication.getId(), VehicleApplicationStatusEnum.DRAFT);
		VehicleApplication toReject = new VehicleApplication();
		toReject.setId(toCheck.getId());
		toReject.setStatus(VehicleApplicationStatusEnum.REJECT.getKey());
		toReject.setRejectReason(vehicleApplication.getRejectReason());
		vehicleApplicationDao.update(toReject);
	}

	@Override
	public void rent(VehicleRentInfo vehicleRentInfo) {
		VehicleApplication toCheck = checkApplicationStatus(vehicleRentInfo.getId(), VehicleApplicationStatusEnum.DRAFT);
		if (!VehicleApplicationUsageEnum.RENT.getKey().equals(toCheck.getUsage())) {
			logger.error("当前派车单不能租赁 : {}", toCheck);
			throw new BusinessException("当前派车单不能租赁");
		}
		VehicleApplication toRent = new VehicleApplication();
		Company checkCompany = companyDao.findById(vehicleRentInfo.getCompanyId());
		if (CompanyStatusEnum.DISABLE.getKey().equals(checkCompany.getStatus())) {
			logger.error("不能对未启用第三方公司进行租赁行为 : {}", checkCompany);
			throw new BusinessException("不能对未启用第三方公司进行租赁行为");
		}
		String drivers = vehicleRentInfo.getDriverName() + "(" + vehicleRentInfo.getDriverPhone() + ")";
		toRent.setId(toCheck.getId());
		toRent.setCompanyId(vehicleRentInfo.getCompanyId());
		toRent.setDrivers(drivers);
		toRent.setPlateNos(vehicleRentInfo.getPlateNos());
		toRent.setStatus(VehicleApplicationStatusEnum.SEND.getKey());
		logger.info("租赁派车 : {}", toRent);
		vehicleApplicationDao.update(toRent);
	}

	@Override
	public void allow(VehicleAllowInfo vehicleAllowInfo) {
		VehicleApplication toCheck = checkApplicationStatus(vehicleAllowInfo.getId(), VehicleApplicationStatusEnum.DRAFT);
		if (!VehicleApplicationUsageEnum.OFFICIAL.getKey().equals(toCheck.getUsage())) {
			logger.error("当前派车单不能进行派车 : {}", toCheck);
			throw new BusinessException("当前派车单不能进行派车");
		}

		String plateNos = getPlateNosFromVehicleId(vehicleAllowInfo);
		String driversInfo = getDriversInfoFromDriverIds(vehicleAllowInfo);
		vehicleDao.occupyVehicleById(vehicleAllowInfo.getVehicleIds());
		insertVehicleLog(vehicleAllowInfo);
		VehicleApplication toAllow = new VehicleApplication();
		toAllow.setId(toCheck.getId());
		toAllow.setAssignerId(vehicleAllowInfo.getAssignerId());
		toAllow.setDispatchTime(new Date());
		toAllow.setPlateNos(plateNos);
		toAllow.setDrivers(driversInfo);
		toAllow.setStartTime(vehicleAllowInfo.getStartTime());
		toAllow.setEndTime(vehicleAllowInfo.getEndTime());
		toAllow.setRemark(vehicleAllowInfo.getRemark());
		toAllow.setStatus(VehicleApplicationStatusEnum.SEND.getKey());
		vehicleApplicationDao.update(toAllow);
	}

	private void insertVehicleLog(VehicleAllowInfo vehicleAllowInfo) {
		List<VehicleLog> vehicleLogList = vehicleAllowInfo.getVehicleIds().stream().map(info -> {
			VehicleLog vehicleLog = new VehicleLog();
			vehicleLog.setStartTime(vehicleAllowInfo.getStartTime());
			vehicleLog.setEndTime(vehicleAllowInfo.getEndTime());
			vehicleLog.setApplicationId(vehicleAllowInfo.getId());
			vehicleLog.setVehicleId(info);
			return vehicleLog;
		}).collect(Collectors.toList());

		int size = vehicleLogList.size() / DATA_SIZE + 1;

		for (int i = 0; i < size; i++) {
			List<VehicleLog> partOfAdd = vehicleLogList.stream().limit(DATA_SIZE).collect(Collectors.toList());
			vehicleLogDao.batchAdd(partOfAdd);
			List<VehicleLog> hasUpdate = vehicleLogList.subList(0, vehicleLogList.size() > DATA_SIZE ? DATA_SIZE : vehicleLogList.size());
			hasUpdate.clear();
		}
	}

	@Override
    public void end(VehicleApplication vehicleApplication) {
		VehicleApplication toCheck = checkApplicationStatus(vehicleApplication.getId(), VehicleApplicationStatusEnum.SEND);
		if (!VehicleApplicationUsageEnum.OFFICIAL.getKey().equals(toCheck.getUsage())) {
			logger.error("当前派车单不能结束用车 : {}", toCheck);
			throw new BusinessException("租赁用车无法结束派车单");
		}
		// 释放车辆占用状态
		vehicleDao.releaseVehicleByApplicationId(toCheck.getId());
		VehicleApplication toReject = new VehicleApplication();
		toReject.setId(toCheck.getId());
		toReject.setStatus(VehicleApplicationStatusEnum.END.getKey());
		// 结束用车
		logger.info("结束用车 : {}", toReject);
		vehicleApplicationDao.update(toReject);
    }

	/**
	 * 删除VehicleApplication
	 * @param id
	 */
	@Override
	public void delete(Long id){
		vehicleApplicationDao.delete(id);
	}

	/**
	 * 生成派车单
	 * 规则 : 拼音首字母-年月日-当日派单号
	 * 如公务用车 2018年12月1日第二张派车单 : GW-20181201-0002
	 * @param usage
	 * @return
	 */
	private String generateApplyNo(VehicleApplicationUsageEnum usage) {
		LocalDate now = LocalDate.now();
		String month = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : String.valueOf(now.getMonthValue());
		String day = now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : String.valueOf(now.getDayOfMonth());
		String applyNo = usage.getPrefix() + "-" + now.getYear() + month + day + "-0001";
		String lastApplyNo = vehicleApplicationDao.findByApplyNo(usage.getPrefix());
		if (!StringUtils.isEmpty(lastApplyNo)) {
			String serialNumber = String.valueOf(Integer.valueOf(lastApplyNo.substring(12)) + 1);
			applyNo = applyNo.substring(0, applyNo.length() - serialNumber.length()) + serialNumber;
		}
		return applyNo;

	}

	private VehicleApplication checkApplicationStatus(Long id, VehicleApplicationStatusEnum expectStatus) {
		VehicleApplication toCheck = vehicleApplicationDao.findById(id);
		if (toCheck == null) {
			logger.error("没有查到该派车单");
			throw new BusinessException("没有查到该派车单");
		}
		if (!expectStatus.getKey().equals(toCheck.getStatus())) {
			logger.error("当前派车单状态不能进行 {} 操作", expectStatus.getDescription());
			throw new BusinessException("当前派车单状态不能进行" + expectStatus.getDescription() + "操作");
		}
		return toCheck;
	}

	private String getPlateNosFromVehicleId(VehicleAllowInfo vehicleAllowInfo) {
		List<SimpleVehicleVO> vehicleVOList = vehicleDao.findByIds(vehicleAllowInfo.getVehicleIds());
		if (CollectionUtils.isNotEmpty(findDisableVehicle(vehicleVOList))) {
			logger.error("选中车辆中含有未启用车辆 : {}", vehicleVOList);
			throw new BusinessException("选中车辆中含有未启用车辆");
		}
		// 去除无效车辆ID
		vehicleAllowInfo.setVehicleIds(vehicleVOList.stream().map(SimpleVehicleVO::getId).collect(Collectors.toList()));
		String plateNos = Arrays.toString(vehicleVOList.stream().map(vo -> vo.getPlateNo()).toArray());
		plateNos = plateNos.substring(1, plateNos.length() - 1);
		return plateNos;
	}

	private List<SimpleVehicleVO> findDisableVehicle(List<SimpleVehicleVO> vehicleVOList) {
		return vehicleVOList.stream().filter(vo -> VehicleStatusEnum.DISABLE.getKey().equals(vo.getStatus()))
				.collect(Collectors.toList());
	}

	private String getDriversInfoFromDriverIds(VehicleAllowInfo vehicleAllowInfo) {
		List<SimpleDriverVO> driverVOList = driverDao.findByIds(vehicleAllowInfo.getDriverIds());
		if (CollectionUtils.isNotEmpty(findDisableDriver(driverVOList))) {
			logger.error("选中驾驶员中含有未启用驾驶员 : {}", driverVOList);
			throw new BusinessException("不能选择为启用驾驶员");
		}
		// 去除无效驾驶员ID
		vehicleAllowInfo.setDriverIds(driverVOList.stream().map(SimpleDriverVO::getId).collect(Collectors.toList()));
		String drivers = Arrays.toString(driverVOList.stream().map(vo -> vo.getName() + "(" + vo.getMobile() + ")").toArray());
		drivers = drivers.substring(1, drivers.length() - 1);
		return drivers;
	}

	private List<SimpleDriverVO> findDisableDriver(List<SimpleDriverVO> driverVOList) {
		return driverVOList.stream().filter(vo -> DriverStatusEnum.DISABLE.getKey().equals(vo.getStatus()))
				.collect(Collectors.toList());
	}
}
