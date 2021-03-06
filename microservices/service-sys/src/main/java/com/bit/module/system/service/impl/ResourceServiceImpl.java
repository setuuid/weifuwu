package com.bit.module.system.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.system.bean.Resource;
import com.bit.module.system.dao.ResourceDao;
import com.bit.module.system.dao.RoleRelResourceDao;
import com.bit.module.system.service.ResourceService;
import com.bit.module.system.vo.ResourceVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Resource的Service实现类
 * @author liqi
 *
 */
@Service("resourceService")
public class ResourceServiceImpl extends BaseService implements ResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private RoleRelResourceDao roleRelResourceDao;

    /**
     * 根据条件查询Resource
     * @param resourceVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(ResourceVO resourceVO){
        PageHelper.startPage(resourceVO.getPageNum(), resourceVO.getPageSize());
        List<Resource> list = resourceDao.findByConditionPage(resourceVO);
        PageInfo<Resource> pageInfo = new PageInfo<Resource>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 通过主键查询单个Resource
     * @param id
     * @return
     */
    @Override
    public Resource findById(Long id){
        return resourceDao.findById(id);
    }

    /**
     * 保存Resource
     * @param resource
     */
    @Override
    @Transactional
    public void add(Resource resource){
        resourceDao.add(resource);
    }

    /**
     * 更新Resource
     * @param resource
     */
    @Override
    @Transactional
    public void update(Resource resource){
        resourceDao.update(resource);
    }

    /**
     * 删除Resource
     * @param ids
     */
    @Override
    @Transactional
    public void batchDelete(List<Long> ids){
        resourceDao.batchDelete(ids);
    }

    /**
     * 查询---树---任何参数 资源所有参数
     * @param resource
     * @return
     */
    @Override
    public List<Resource> findAllTreeByParam(Resource resource) {
        List<Resource> resourceList = resourceDao.findAllByParam(resource);
        List<Resource> rootResources = new ArrayList<>();
        for (Resource obj : resourceList) {
            if (obj.getPid() == Const.ROOT_RESOURCE_PID) {
                rootResources.add(obj);
            }
        }
        for (Resource rootResource : rootResources) {
            rootResource.setChildList(getChildList(rootResource.getId(),resourceList));
        }
        return rootResources;
    }

    /**
     * 查询--树 --userId ，tid ，资源参数
     * @param resource
     * @return
     */
    @Override
    public List<Resource> findTreeByParam(Resource resource) {
        UserInfo currentUserInfo = getCurrentUserInfo();
        //终端id
        resource.setTerminalId(currentUserInfo.getTid());
        //用户id
        resource.setUserId(currentUserInfo.getId());
        return this.findAllTreeByParam(resource);
    }

    /**
     * 条件查询资源
     * @param resource
     * @return
     */
    @Override
    public List<Resource> findAllByParam(Resource resource) {
       return  resourceDao.findAllByParam(resource);
    }

    /**
     * 查看角色权限--树---tid，roleId，资源参数
     * @param resource
     * @return
     */
    @Override
    public List<Resource> findRolePermssion(Resource resource) {
        UserInfo currentUserInfo = getCurrentUserInfo();
        //终端id
        resource.setTerminalId(currentUserInfo.getTid());
        return this.findAllTreeByParam(resource);
    }

    /**
     * 递归查找子菜单
     * @param id  当前菜单id
     * @param resourceList
     * @return 菜单数据
     */
    private List<Resource> getChildList(Long id, List<Resource> resourceList) {
        // 子菜单
        List<Resource> childList = new ArrayList<>();
        for (Resource resource : resourceList) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (resource.getPid()!=null) {
                if (resource.getPid().equals(id)) {
                    childList.add(resource);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Resource resource : childList) {// 没有url子菜单还有子菜单
            resource.setChildList(getChildList(resource.getId(), resourceList));
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    /**
     * 删除Resource （包括删除下级的所有）
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id){
        //1查询所有
        List<Resource> all = resourceDao.findAllByParam(null);
        //2接收下级的所有
        List<Resource> list = new ArrayList<>();
        //3下级所有的id集合
        List<Long> ids = new ArrayList<>();
        //4递归查询下级集合并放入第2步的list
        this.findChildList(list,all,id);
        if (list.size()>0){
            for (Resource resource : list) {
                ids.add(resource.getId());
            }
        }
        if (ids.size()>0){
            //5把第3步的id集合当作参数批量删除
            resourceDao.batchDelete(ids);
            //删除资源和角色的关系批量删除
            roleRelResourceDao.batchDeleteByResourceIds(ids);
        }
        //6删除传过来的父级节点删除
        resourceDao.delete(id);
        //删除资源和角色的关系
        roleRelResourceDao.delByResourceId(id);
    }

    /**
     * 递归查询---整个方法 用来查询出下级的所有符合要求的对象放在list里面 做批量删除  也就是说当*****删除一级菜单***吧下级菜单也全部删除
     * @param list 新的一个list 把符合要求的都放在这个list里面
     * @param all 查询出来总的list
     * @param id 前端传来的id
     */
    private void findChildList(List<Resource> list, List<Resource> all,Long id) {
        List<Resource> resourceList = new ArrayList<>();
        for (Resource resource : all) {
            if (resource.getPid()!=null){
                if (resource.getPid().equals(id)) {
                    resourceList.add(resource);
                    list.add(resource);
                }
            }
        }
        if (resourceList.size()>0){
            for (Resource resource : resourceList) {
                if (resource!=null){
                    findChildList(list,all,resource.getId());
                }
            }
        }else {
            return;
        }
    }

}
