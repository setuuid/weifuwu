package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Study;
import com.bit.module.pb.vo.StudyVO;

/**
 * Study的Service
 */
public interface StudyService {
    /**
     * 新增学习计划
     */
    void add(Study study);
    /**
     * 修改学习计划
     */
    void update(Study study);
    /**
     * 删除学习计划
     */
    void delete(Long id);
    /**
     * 发布学习计划
     */
    void release(Study study);
    /**
     * 查看学习计划
     */
    BaseVo queryById(Long id);
    /**
     * 分页查询和根据条件查询（主题、发布时间范围、发布状态）共用接口
     */
    BaseVo listPage(StudyVO studyVO);

}
