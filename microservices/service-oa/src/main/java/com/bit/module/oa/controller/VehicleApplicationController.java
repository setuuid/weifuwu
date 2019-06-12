package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.VehicleApplication;
import com.bit.module.oa.service.VehicleApplicationService;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.vehicleApplication.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * VehicleApplication的相关请求
 *
 * @author generator
 */
@RestController
@RequestMapping(value = "/vehicleApplication")
public class VehicleApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleApplicationController.class);
    @Autowired
    private VehicleApplicationService vehicleApplicationService;


    /**
     * 分页查询VehicleApplication列表
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody VehicleApplicationVO vehicleApplicationVO) {
        //分页对象，前台传递的包含查询的参数

        return vehicleApplicationService.findByConditionPage(vehicleApplicationVO);
    }

    @PostMapping("/use/listPage")
    public BaseVo myVehicleUsingListPage(@RequestBody VehicleApplicationQO vehicleApplicationQO) {
        vehicleApplicationQO.checkMyVehiclePage();
        return vehicleApplicationService.findMyVehicleApplicationPage(vehicleApplicationQO);
    }

    @PostMapping("/use/export")
    public void myVehicleUsingExport(@RequestParam(name = "userId") Long userId,
                                     @RequestParam(name = "planStartTime", required = false) Date planStartTime,
                                     @RequestParam(name = "planEndTime", required = false) Date planEndTime,
                                     @RequestParam(name = "minApplyTime", required = false) Date minApplyTime,
                                     @RequestParam(name = "maxApplyTime", required = false) Date maxApplyTime,
                                     @RequestParam(name = "nature", required = false) Integer nature,
                                     @RequestParam(name = "usage", required = false) Integer usage,
                                     @RequestParam(name = "status", required = false) Integer status,
                                     HttpServletResponse response) {
        try {
            VehicleApplicationQO vehicleApplicationQO = setProperty(userId, planStartTime, planEndTime, minApplyTime,
                    maxApplyTime, nature, usage, status);
            List<MyVehicleApplicationExportVO> myVehicleApplicationExportVOS = vehicleApplicationService.findExportMyVehicleUsing(vehicleApplicationQO);
            AtomicLong num = new AtomicLong(1L);
            myVehicleApplicationExportVOS.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, myVehicleApplicationExportVOS, MyVehicleApplicationExportVO.class, "我的用车");
        } catch (IOException e) {
            logger.error("我的用车导出异常 : {}", e);
        }
    }

    private VehicleApplicationQO setProperty(@RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "planStartTime", required = false) Date planStartTime,
                                             @RequestParam(name = "planEndTime", required = false) Date planEndTime,
                                             @RequestParam(name = "minApplyTime", required = false) Date minApplyTime,
                                             @RequestParam(name = "maxApplyTime", required = false) Date maxApplyTime,
                                             @RequestParam(name = "nature", required = false) Integer nature,
                                             @RequestParam(name = "usage", required = false) Integer usage,
                                             @RequestParam(name = "status", required = false) Integer status) {
        VehicleApplicationQO vehicleApplicationQO = new VehicleApplicationQO();
        vehicleApplicationQO.setUserId(userId);
        vehicleApplicationQO.setPlanStartTime(planStartTime);
        vehicleApplicationQO.setPlanEndTime(planEndTime);
        vehicleApplicationQO.setMaxApplyTime(maxApplyTime);
        vehicleApplicationQO.setMinApplyTime(minApplyTime);
        vehicleApplicationQO.setNature(nature);
        vehicleApplicationQO.setUsage(usage);
        vehicleApplicationQO.setStatus(status);
        return vehicleApplicationQO;
    }

    @PostMapping("/ledger/listPage")
    public BaseVo ledgerListPage(@RequestBody VehicleApplicationQO vehicleApplicationQO) {
        return vehicleApplicationService.findLedgerApplicationListPage(vehicleApplicationQO);
    }

    @PostMapping("/ledger/export")
    public void ledgerExport(@RequestParam(name = "planStartTime", required = false) Date planStartTime,
                             @RequestParam(name = "planEndTime", required = false) Date planEndTime,
                             @RequestParam(name = "minApplyTime", required = false) Date minApplyTime,
                             @RequestParam(name = "maxApplyTime", required = false) Date maxApplyTime,
                             @RequestParam(name = "nature", required = false) Integer nature,
                             @RequestParam(name = "usage", required = false) Integer usage,
                             HttpServletResponse response) {
        try {
            VehicleApplicationQO vehicleApplicationQO = setProperty(null, planStartTime, planEndTime, minApplyTime,
                    maxApplyTime, nature, usage, null);
            List<HandleVehicleApplicationExportVO> driverExportVOList = vehicleApplicationService.findLedgerApplicationExport(vehicleApplicationQO);
            AtomicLong num = new AtomicLong(1L);
            driverExportVOList.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, driverExportVOList, HandleVehicleApplicationExportVO.class, "车辆台账");
        } catch (IOException e) {
            logger.error("车辆台账导出异常 : {}", e);
        }
    }

    @PostMapping("/handle/listPage")
    public BaseVo handleListPage(@RequestBody VehicleApplicationQO vehicleApplicationQO) {
        return vehicleApplicationService.findHandleApplicationListPage(vehicleApplicationQO);
    }

    @PostMapping("/handle/export")
    public void handleListExport(@RequestParam(name = "planStartTime", required = false) Date planStartTime,
                                 @RequestParam(name = "planEndTime", required = false) Date planEndTime,
                                 @RequestParam(name = "minApplyTime", required = false) Date minApplyTime,
                                 @RequestParam(name = "maxApplyTime", required = false) Date maxApplyTime,
                                 @RequestParam(name = "nature", required = false) Integer nature,
                                 @RequestParam(name = "usage", required = false) Integer usage,
                                 @RequestParam(name = "status", required = false) Integer status,
                                 HttpServletResponse response) {
        try {
            VehicleApplicationQO vehicleApplicationQO = setProperty(null, planStartTime, planEndTime, minApplyTime,
                    maxApplyTime, nature, usage, status);
            List<HandleVehicleApplicationExportVO> driverExportVOList = vehicleApplicationService.findHandleApplicationExport(vehicleApplicationQO);
            AtomicLong num = new AtomicLong(1L);
            driverExportVOList.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, driverExportVOList, HandleVehicleApplicationExportVO.class, "派车管理");
        } catch (IOException e) {
            logger.error("派车管理导出异常 : {}", e);
        }
    }

    /**
     * 根据主键ID查询VehicleApplication
     * @param toQuery
     * @return
     */
    @PostMapping("/detail")
    public BaseVo query(@RequestBody VehicleApplication toQuery) {
        VehicleApplicationDetail vehicleApplication = vehicleApplicationService.findVehicleApplicationDetail(toQuery);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(vehicleApplication);
        return baseVo;
    }

    /**
     * 申请用车
     *
     * @param vehicleApplication VehicleApplication实体
     * @return
     */
    @PostMapping("/use/apply")
    public BaseVo apply(@Validated(VehicleApplication.Apply.class) @RequestBody VehicleApplication vehicleApplication) {
        vehicleApplicationService.apply(vehicleApplication);
        return new BaseVo();
    }

    /**
     * 修改VehicleApplication
     *
     * @param vehicleApplication VehicleApplication实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody VehicleApplication vehicleApplication) {
        vehicleApplicationService.update(vehicleApplication);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据主键ID删除VehicleApplication
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        vehicleApplicationService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    @PostMapping("/handle/reject")
    public BaseVo reject(@RequestBody VehicleApplication vehicleApplication) {
        vehicleApplication.checkRejectParam();
        vehicleApplicationService.reject(vehicleApplication);
        return new BaseVo();
    }

    @PostMapping("/handle/rent")
    public BaseVo rent(@RequestBody VehicleRentInfo vehicleRentInfo) {
        vehicleRentInfo.checkRent();
        vehicleApplicationService.rent(vehicleRentInfo);
        return new BaseVo();
    }

    @PostMapping("/handle/allow")
    public BaseVo allow(@RequestBody VehicleAllowInfo vehicleAllowInfo) {
        vehicleApplicationService.allow(vehicleAllowInfo);
        return new BaseVo();
    }

    @PostMapping("/handle/end")
    public BaseVo end(@RequestBody VehicleApplication vehicleApplication) {
        vehicleApplicationService.end(vehicleApplication);
        return new BaseVo();
    }
}
