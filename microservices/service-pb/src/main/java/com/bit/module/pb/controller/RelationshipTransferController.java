package com.bit.module.pb.controller;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Done;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.bean.PartyMemberSummary;
import com.bit.module.pb.bean.RelationshipTransfer;
import com.bit.module.pb.enums.ApprovalStatusEnum;
import com.bit.module.pb.service.DoneService;
import com.bit.module.pb.service.OrganizationService;
import com.bit.module.pb.service.RelationshipTransferService;
import com.bit.module.pb.vo.PartyMemberSummaryVO;
import com.bit.module.pb.vo.RelationshipTransferParamsVO;
import com.bit.module.pb.vo.RelationshipTransferVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RelationshipTransfer的相关请求
 *
 * @author generator
 */
@Slf4j
@RestController
@RequestMapping(value = "/relationshipTransfer")
public class RelationshipTransferController {

    @Autowired
    private RelationshipTransferService relationshipTransferService;

    @Autowired
    private DoneService doneService;

    @Autowired
    private OrganizationService organizationService;

    /**
     * 根据主键ID查询RelationshipTransfer
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        // 申请信息
        RelationshipTransfer relationshipTransfer = relationshipTransferService.findById(id);
        // 审批信息
        List<Done> dones = doneService.findRecord(id);
        Map<String, Object> result = new HashMap<>();
        result.put("relationshipTransfer", relationshipTransfer);
        result.put("dones", dones);
        return new BaseVo(result);
    }

    /**
     * 新增RelationshipTransfer
     *
     * @param relationshipTransfer RelationshipTransfer实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody RelationshipTransferVO relationshipTransfer) {
        relationshipTransferService.add(relationshipTransfer);
        return new BaseVo();
    }

    /**
     * 接收镇外党员
     *
     * @param partyMemberSummaryVO
     * @return
     */
    @PostMapping("/reception")
    public BaseVo reception(@Valid @RequestBody PartyMemberSummaryVO partyMemberSummaryVO) {
        PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
        BeanUtils.copyProperties(partyMemberSummaryVO, partyMemberSummary);

        Organization organization = organizationService.findById(partyMemberSummaryVO.getOrgId());
        partyMemberSummary.setOrgName(organization.getName());
        relationshipTransferService.reception(partyMemberSummary);
        return new BaseVo();
    }

    /**
     * 修改RelationshipTransfer
     *
     * @param relationshipTransferVO RelationshipTransfer实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody RelationshipTransferVO relationshipTransferVO) {
        relationshipTransferService.update(relationshipTransferVO);
        return new BaseVo();
    }

    /**
     * 更改镇外党员内转信息
     *
     * @param partyMemberSummaryVO
     * @return
     */
    @PostMapping("/modify/proposer")
    public BaseVo modifyProposer(@Valid @RequestBody PartyMemberSummaryVO partyMemberSummaryVO) {
        PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
        BeanUtils.copyProperties(partyMemberSummaryVO, partyMemberSummary);
        relationshipTransferService.updateReception(partyMemberSummary);
        return new BaseVo();
    }

    /**
     * 提交申请
     *
     * @param params
     * @return
     */
    @Transactional
    @PostMapping("/submit")
    public BaseVo submit(@RequestBody Map<String, Object> params) {
        Object id = params.get("id");
        if (id == null) {
            throw new BusinessException("缺少ID参数");
        }
        // 更改状态
        relationshipTransferService.updateFlow(Long.valueOf(id.toString()), ApprovalStatusEnum.AUDIT.getValue(), "提交申请");
        return new BaseVo();
    }

    /**
     * 接收
     *
     * @param params
     * @return
     */
    @Transactional
    @PostMapping("/receive")
    public BaseVo receive(@RequestBody Map<String, Object> params) {
        Object id = params.get("id");
        if (id == null) {
            throw new BusinessException("缺少ID参数");
        }
        // 更新流程状态
        relationshipTransferService.updateFlow(Long.valueOf(id.toString()), ApprovalStatusEnum.RECEIVE.getValue(), null);
        return new BaseVo();
    }

    /**
     * 审核通过
     *
     * @param params
     * @return
     */
    @Transactional
    @PostMapping("/pass")
    public BaseVo pass(@RequestBody Map<String, Object> params) {
        Object id = params.get("id");
        if (id == null) {
            throw new BusinessException("缺少ID参数");
        }
        // 更新流程状态
        relationshipTransferService.updateFlow(Long.valueOf(id.toString()), ApprovalStatusEnum.PASSED.getValue(), null);
        return new BaseVo();
    }

    /**
     * 退回申请
     *
     * @param params
     * @return
     */
    @Transactional
    @PostMapping("/sendBack")
    public BaseVo sendBack(@RequestBody Map<String, Object> params) {
        Object id = params.get("id");
        Object reason = params.get("reason");
        if (id == null) {
            throw new BusinessException("缺少ID参数");
        }
        if (reason == null) {
            throw new BusinessException("缺少原因参数");
        }
        relationshipTransferService.updateFlow(Long.valueOf(id.toString()), ApprovalStatusEnum.RETURN.getValue(), reason == null ? null : reason.toString());
        return new BaseVo();
    }

    /**
     * 党员转出列表分页(T2、T3)
     *
     * @param relationshipTransferVO
     * @return
     */
    @PostMapping("/rollout/listPage")
    public BaseVo outListPage(@RequestBody RelationshipTransferVO relationshipTransferVO) {
        //分页对象，前台传递的包含查询的参数
        PageInfo pageInfo =  relationshipTransferService.findByOutListPage(relationshipTransferVO);
        return new BaseVo(pageInfo);
    }

    /**
     * 党员接收列表
     *
     * @param relationshipTransferVO
     * @return
     */
    @PostMapping("/reception/listPage")
    public BaseVo receptionPage(@RequestBody RelationshipTransferVO relationshipTransferVO) {
        //分页对象，前台传递的包含查询的参数
        PageInfo pageInfo = relationshipTransferService.findByReceptionPage(relationshipTransferVO);
        return new BaseVo(pageInfo);
    }

    /**
     * 党员列表分页(T1)
     *
     * @param paramsVO
     * @return
     */
    @PostMapping("/transfer/listPage")
    public BaseVo inListPage(@RequestBody RelationshipTransferParamsVO paramsVO) {
        //分页对象，前台传递的包含查询的参数
        PageInfo pageInfo = relationshipTransferService.findByTransferPage(paramsVO);
        return new BaseVo(pageInfo);
    }

    /**
     * 根据主键ID删除RelationshipTransfer
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        relationshipTransferService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }
}
