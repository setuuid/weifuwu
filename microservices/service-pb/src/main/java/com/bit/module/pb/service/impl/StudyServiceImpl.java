package com.bit.module.pb.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.pb.bean.Study;
import com.bit.module.pb.dao.StudyDao;
import com.bit.module.pb.service.StudyService;
import com.bit.module.pb.vo.StudyVO;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * StudyService的实现类
 * @author zhangjie
 * @date 2019-1-11
 */
@Service("studyService")
public class StudyServiceImpl implements StudyService {

    private static final Logger logger = LoggerFactory.getLogger(StudyServiceImpl.class);

    @Autowired
    private StudyDao studyDao;
    /**
     * 新增学习计划
     * @param study
     */
    @Override
    @Transactional
    public void add(Study study) {
        //从登陆信息中获取用户id
        study.setCreateUserId(new Long(1));

        study.setIsRelease(Const.IS_RELEASE_YES);
        study.setCreateTime(new Date());
        studyDao.add(study);
    }

    /**
     * 修改学习计划
     * @param study
     */
    @Override
    @Transactional
    public void update(Study study) {
        //从登陆信息中获取用户id
        study.setUpdateUserId(new Long(1L));
        study.setUpdateTime(new Date());
        studyDao.update(study);
    }

    /**
     * 删除学习计划
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        studyDao.delete(id);
    }

    /**
     * 发布学习计划
     * @param study
     */
    @Override
    @Transactional
    public void release(Study study) {
        //从登录用户信息中获取该用户所属组织机构
        study.setPborgId(new Long(72057594037927936L));

        study.setReleaseTime(new Date());
        study.setIsRelease(study.getIsRelease());
        studyDao.update(study);
    }

    /**
     * 查看学习计划
     * @param id
     * @return Study
     */
    @Override
    public BaseVo queryById(Long id) {
        BaseVo baseVo = new BaseVo();

        Study study = studyDao.queryById(id);
        Map<String,Object> map = new HashMap();
        map.put("id",study.getId());
        map.put("theme",study.getTheme());
        map.put("content",study.getContent());
        map.put("isRelease",study.getIsRelease());
        map.put("pborgName",study.getName());
        map.put("releaseTime",study.getReleaseTime());

        String attachmentId = study.getAttachmentId();
        //遍历附件id查询附件名、类型，判断类型为图片的返回图片名并返回url；类型为文件的只显示附件名
        Map<String,Object> am = new HashMap();
        am.put("attachmentName",study.getTheme());
        am.put("type",0);
        am.put("url","http://200.200.3.39:8888/group1/M00/00/02/yMgDJ1wSC5-AIv3wAAJgdj_mj1M739.jpg");

        List<Object> o = new ArrayList<>();
        o.add(am);
        map.put("attachment",o);

        baseVo.setData(map);
        return baseVo;

    }

    /**
     * 分页查询和根据条件查询（主题、发布时间范围、发布状态）共用接口
     * @param studyVO
     * @return study
     */
    @Override
    public BaseVo listPage(StudyVO studyVO) {
        PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
        if(StringUtil.isNotEmpty(studyVO.getTheme())){
            String theme = studyVO.getTheme()+"%";
            studyVO.setTheme(theme);
        }

        List<Study> list = studyDao.listPage(studyVO);
        PageInfo pageInfo = new PageInfo(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

}
