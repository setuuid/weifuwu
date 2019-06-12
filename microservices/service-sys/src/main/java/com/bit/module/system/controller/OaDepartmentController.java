package com.bit.module.system.controller;

import javax.validation.Valid;

import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.service.OaDepartmentService;
import com.bit.module.system.vo.OaDepartmentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bit.base.vo.BaseVo;

import java.util.List;

/**
 * OaDepartment的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/oaDepartment")
public class OaDepartmentController {
	private static final Logger logger = LoggerFactory.getLogger(OaDepartmentController.class);
	@Autowired
	private OaDepartmentService oaDepartmentService;
	

    /**
     * 分页查询OaDepartment列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody OaDepartmentVO oaDepartmentVO) {
    	//分页对象，前台传递的包含查询的参数
        return oaDepartmentService.findByConditionPage(oaDepartmentVO);
    }

    /**
     * 根据主键ID查询OaDepartment
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Integer id) {

        OaDepartment oaDepartment = oaDepartmentService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(oaDepartment);
		return baseVo;
    }
    
    /**
     * 新增OaDepartment
     *
     * @param oaDepartment OaDepartment实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody OaDepartment oaDepartment) {
        oaDepartmentService.add(oaDepartment);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改OaDepartment
     *
     * @param oaDepartment OaDepartment实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody OaDepartment oaDepartment) {
		oaDepartmentService.update(oaDepartment);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除OaDepartment
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Integer id) {
        oaDepartmentService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 政务列表-不分页
     * @return
     */
    @PostMapping("/getAll")
    public BaseVo getAll() {
        List<OaDepartment> all = oaDepartmentService.findAll("");
        BaseVo baseVo = new BaseVo();
        baseVo.setData(all);
        return baseVo;
    }
}
