package com.bit.module.pb.dao;

import com.bit.module.pb.bean.Study;
import com.bit.module.pb.vo.StudyVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Study的Dao
 * @author zhangjie
 * @date 2019-1-11
 */
public interface StudyDao {

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
    void delete(@Param(value = "id") Long id);
    /**
     * 查看学习计划
     */
    Study queryById(@Param(value = "id") Long id);
    /**
     * 分页查询
     */
    List<Study> listPage(StudyVO studyVO);


}
