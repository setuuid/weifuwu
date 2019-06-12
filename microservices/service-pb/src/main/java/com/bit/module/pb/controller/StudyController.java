package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Study;
import com.bit.module.pb.service.StudyService;
import com.bit.module.pb.vo.StudyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Study的相关请求
 * @author zhangjie
 * @date 2019-1-11
 */
@RestController
@RequestMapping("/study")
public class StudyController {
    private static final Logger logger = LoggerFactory.getLogger(StudyController.class);

    @Autowired
    private StudyService studyService;
    /**
     * 新增学习计划
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Study study){
        studyService.add(study);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 修改学习计划
     * @param study
     * @return
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody Study study){
        studyService.update(study);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 删除学习计划
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id){
        studyService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 发布学习计划
     * @param study
     * @return
     */
    @PostMapping("/release")
    public BaseVo release(@RequestBody Study study){
        studyService.release(study);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 查看学习计划
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo queryById(@PathVariable(value = "id")Long id){
        return studyService.queryById(id);
    }

    /**
     * 分页查询
     * @param studyVO
     * @retutn BaseVo
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody StudyVO studyVO){
        return studyService.listPage(studyVO);
    }

}
