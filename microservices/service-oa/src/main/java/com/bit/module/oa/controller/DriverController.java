package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Driver;
import com.bit.module.oa.enums.DriverStatusEnum;
import com.bit.module.oa.service.DriverService;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.driver.DriverExportVO;
import com.bit.module.oa.vo.driver.DriverVO;
import com.bit.module.oa.vo.driver.SimpleDriverVO;
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
 * Driver的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/driver")
public class DriverController {
	private static final Logger logger = LoggerFactory.getLogger(DriverController.class);
	@Autowired
	private DriverService driverService;
	

    /**
     * 分页查询Driver列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody DriverVO driverVO) {
    	//分页对象，前台传递的包含查询的参数

        return driverService.findByConditionPage(driverVO);
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            List<DriverExportVO> driverExportVOList = driverService.findExportDriver();
            AtomicLong num = new AtomicLong(1L);
            driverExportVOList.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, driverExportVOList, DriverExportVO.class, "驾驶员管理");
        } catch (IOException e) {
            logger.error("驾驶员管理导出异常 : {}", e);
        }
    }

    /**
     * 查询驾驶员详情
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Driver driver = driverService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(driver);
		return baseVo;
    }
    
    /**
     * 新增Driver
     *
     * @param driver Driver实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Validated(Driver.Add.class) @RequestBody Driver driver) {
        driverService.add(driver);
        return new BaseVo();
    }

    /**
     * 修改Driver
     *
     * @param driver Driver实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Validated(Driver.Update.class) @RequestBody Driver driver) {
        driverService.update(driver);
        return new BaseVo();
    }

    @PostMapping("/{id}/enable")
    public BaseVo enableDriver(@PathVariable("id") Long id) {
        driverService.convertStatus(id, DriverStatusEnum.ENABLED.getKey());
        return new BaseVo();
    }

    @PostMapping("/{id}/disable")
    public BaseVo disableDriver(@PathVariable("id") Long id) {
        driverService.convertStatus(id, DriverStatusEnum.DISABLE.getKey());
        return new BaseVo();
    }

    /**
     * 根据主键ID删除Driver
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        driverService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    @GetMapping("/list")
    public BaseVo findAll() {
        List<SimpleDriverVO> drivers = driverService.findAll("id");
        return new BaseVo<>(drivers);
    }

    @PostMapping("/mobile/check")
    public BaseVo checkMobile(@RequestBody Driver driver) {
        Boolean exist = driverService.checkDriverMobile(driver.getMobile());
        return new BaseVo(exist);
    }
}
