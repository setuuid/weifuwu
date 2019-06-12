package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Done;
import com.bit.module.pb.service.DoneService;
import com.bit.module.pb.vo.DoneVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Done的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/done")
public class DoneController {
	private static final Logger logger = LoggerFactory.getLogger(DoneController.class);
	@Autowired
	private DoneService doneService;
	

    /**
     * 分页查询Done列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody DoneVO doneVO) {
    	//分页对象，前台传递的包含查询的参数

        return doneService.findByConditionPage(doneVO);
    }

    /**
     * 根据主键ID查询Done
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Done done = doneService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(done);
		return baseVo;
    }
    
    /**
     * 新增Done
     *
     * @param done Done实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody Done done) {
        doneService.add(done);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Done
     *
     * @param done Done实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody Done done) {
		doneService.update(done);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除Done
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        doneService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
