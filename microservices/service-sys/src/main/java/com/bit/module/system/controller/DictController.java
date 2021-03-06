package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Dict;
import com.bit.module.system.service.DictService;
import com.bit.module.system.vo.DictVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dict的相关请求
 * @author zhangjie
 * @date 2018-12-28
 */
@RestController
@RequestMapping(value = "/dict")
public class DictController {

    private static final Logger logger = LoggerFactory.getLogger(DictController.class);

    @Autowired
    private DictService dictService;

    /**
     * 根据条件查询Dict,分页查询
     * @param dictVO
     * @return BaseVo
     */
    @PostMapping("/listPage")
    public BaseVo findByConditionPage(@RequestBody DictVO dictVO){
        return dictService.findByConditionPage(dictVO);
    }

    /**
     * 通过主键查询单个Dict
     * @param id
     * @return Dict
     */
    @GetMapping("/findById")
    public BaseVo findById(@PathVariable(value = "id") Long id){
        BaseVo baseVo = new BaseVo();
        baseVo.setData(dictService.findById(id));
        return baseVo;
    }

    /**
     * 保存Dict
     * @param dict
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Dict dict){
        dictService.add(dict);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 更新Dict
     * @param dict
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody Dict dict){
        dictService.update(dict);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 删除Dict
     * @param id
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id){
        dictService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据模块查询字典
     * @param module
     */
    @GetMapping("/findByModule/{module}")
    public BaseVo findByModule(@PathVariable(value = "module") String module){
        List<Dict> dictList=dictService.findByModule(module);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(dictList);
        return baseVo;
    }

}
