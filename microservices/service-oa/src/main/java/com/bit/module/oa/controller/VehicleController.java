package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Vehicle;
import com.bit.module.oa.enums.VehicleStatusEnum;
import com.bit.module.oa.service.VehicleService;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.vehicle.SimpleVehicleVO;
import com.bit.module.oa.vo.vehicle.VehicleExportVO;
import com.bit.module.oa.vo.vehicle.VehicleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Vehicle的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/vehicle")
public class VehicleController {
	private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
	@Autowired
	private VehicleService vehicleService;
	

    /**
     * 分页查询Vehicle列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody VehicleVO vehicleVO) {
    	//分页对象，前台传递的包含查询的参数

        return vehicleService.findByConditionPage(vehicleVO);
    }

    @PostMapping("/export")
    public void export(@RequestParam(name = "vehicleType", required = false) String vehicleType,
                       @RequestParam(name = "power", required = false) String power,
                       @RequestParam(name = "plateType", required = false) String plateType,
                       HttpServletResponse response) {
        try {
            List<VehicleExportVO> vehicleExportVOList = vehicleService.findExportVehicle(vehicleType, power, plateType);
            AtomicLong num = new AtomicLong(1L);
            vehicleExportVOList.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, vehicleExportVOList, VehicleExportVO.class, "车辆管理");
        } catch (IOException e) {
            logger.error("车辆管理导出异常 : {}", e);
        }
    }
    /**
     * 根据主键ID查询Vehicle
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Vehicle vehicle = vehicleService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(vehicle);
		return baseVo;
    }
    
    /**
     * 新增Vehicle
     *
     * @param vehicle Vehicle实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Validated(Vehicle.Add.class) @RequestBody Vehicle vehicle) {
        vehicleService.add(vehicle);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Vehicle
     *
     * @param vehicle Vehicle实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Validated(Vehicle.Update.class) @RequestBody Vehicle vehicle) {
		vehicleService.update(vehicle);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除Vehicle
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        vehicleService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    @PostMapping("/{id}/enable")
    public BaseVo enableVehicle(@PathVariable("id") Long id) {
        vehicleService.convertStatus(id, VehicleStatusEnum.ENABLED.getKey());
        return new BaseVo();
    }

    @PostMapping("/{id}/disable")
    public BaseVo disableVehicle(@PathVariable("id") Long id) {
        vehicleService.convertStatus(id, VehicleStatusEnum.DISABLE.getKey());
        return new BaseVo();
    }

    @GetMapping("/list")
    public BaseVo findAll() {
        List<SimpleVehicleVO> drivers = vehicleService.findAll("id");
        return new BaseVo<>(drivers);
    }

    @PostMapping("/plateNo/check")
    public BaseVo checkMobile(@RequestBody Vehicle vehicle) {
        Boolean exist = vehicleService.checkPlateNo(vehicle.getPlateNo());
        return new BaseVo(exist);
    }
}
