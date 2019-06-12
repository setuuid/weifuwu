package com.bit.module.system.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Dict;
import com.bit.module.system.dao.DictDao;
import com.bit.module.system.service.DictService;
import com.bit.module.system.vo.DictVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Dict的Service实现类
 * @author zhangjie
 * @date 2018-12-28
 */
@Service("dictService")
public class DictServiceImpl extends BaseService implements DictService {

    private static final Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);

    @Autowired
    private DictDao dictDao;

    /**
     * 根据条件查询Dict,分页查询
     * @param dictVO
     * @return BaseVo
     */
    @Override
    public BaseVo findByConditionPage(DictVO dictVO) {
        PageHelper.startPage(dictVO.getPageNum(), dictVO.getPageSize());
        List<Dict> list = dictDao.findByConditionPage(dictVO);
        PageInfo<Dict> pageInfo = new PageInfo<Dict>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 通过主键查询单个Dict
     * @param id
     * @return Dict
     */
    @Override
    public Dict findById(Long id) {
        return dictDao.findById(id);
    }

    /**
     * 保存Dict
     * @param dict
     */
    @Override
    @Transactional
    public void add(Dict dict) {
        dictDao.add(dict);
    }

    /**
     * 更新Dict
     * @param dict
     */
    @Override
    @Transactional
    public void update(Dict dict) {
        dictDao.update(dict);
    }

    /**
     * 删除Dict
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        dictDao.delete(id);
    }

    /**
     * 根据module 查询字典
     * @param module
     * @return
     */
    @Override
    public List<Dict> findByModule(String module) {
        return dictDao.findByModule(module);
    }
}
