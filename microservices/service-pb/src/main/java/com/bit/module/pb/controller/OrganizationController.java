package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.service.OrganizationService;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.OrganizationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Organization的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {
	private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	@Autowired
	private OrganizationService organizationService;

    /**
     * 分页查询Organization列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody OrganizationVO organizationVO) {
    	//分页对象，前台传递的包含查询的参数
        return organizationService.findByConditionPage(organizationVO);
    }

    /**
     * 根据主键ID查询Organization
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") String id) {

        Organization organization = organizationService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(organization);
		return baseVo;
    }
    
    /**
     * 新增Organization
     *
     * @param organization Organization实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody Organization organization) {
        organizationService.add(organization);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Organization
     *
     * @param organization Organization实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody Organization organization) {
		organizationService.update(organization);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除Organization
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") String id) {
        organizationService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    @PostMapping("/sub/get")
    public BaseVo getSubOrganizationIgnoreItself(@RequestBody Organization organization) {
        List<OrganizationInfoVO> organizations = organizationService.getDirectSubOrganization(organization.getId());
        BaseVo<List<OrganizationInfoVO>> organizationBaseVo = new BaseVo<>();
        organizationBaseVo.setData(organizations);
        return organizationBaseVo;
    }
}
