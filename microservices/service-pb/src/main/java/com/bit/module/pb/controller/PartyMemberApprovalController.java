package com.bit.module.pb.controller;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.enums.ActionPartyMemberEnum;
import com.bit.module.pb.enums.AssigneeTypeEnum;
import com.bit.module.pb.enums.PMStatusEnum;
import com.bit.module.pb.service.*;
import com.bit.module.pb.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PartyMemberApproval的相关请求
 *
 * @author generator
 */
@Slf4j
@RestController
@RequestMapping(value = "/partyMemberApproval")
public class PartyMemberApprovalController {

    @Autowired
    private PartyMemberApprovalService partyMemberApprovalService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private DoneService doneService;

    @Autowired
    private TodoService todoService;

    @Autowired
    private PartyMemberService partyMemberService;

    @Autowired
    private ExServicemanService exServicemanService;

    @Autowired
    private PartyDueService partyDueService;

    /**
     * 分页查询PartyMemberApproval列表
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PartyMemberApprovalVO partyMemberApprovalVO) {
        // 分页对象，前台传递的包含查询的参数
        return partyMemberApprovalService.findByConditionPage(partyMemberApprovalVO);
    }

    /**
     * 查看党员审批信息
     * 根据主键ID查询PartyMemberApproval
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        // 申请信息表
        PartyMemberApproval partyMemberApproval = partyMemberApprovalService.findById(id);
        if (!StringUtils.isEmpty(partyMemberApproval.getMemberId())) {
            PartyMember partyMember = partyMemberService.findById(partyMemberApproval.getMemberId());
            PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
            BeanUtils.copyProperties(partyMember, partyMemberSummary);
            partyMemberApproval.setModification(partyMemberSummary);
        }
        // 审核记录
        List<Done> doneList = doneService.findRecord(id);
        Map<String, Object> result = new HashMap<>(2);
        result.put("partyMemberApproval", partyMemberApproval);
        result.put("doneList", doneList);
        return new BaseVo(result);
    }

    /**
     * 新增PartyMemberApproval
     *
     * @param partyMemberSummaryVO PartyMemberApproval实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody PartyMemberSummaryVO partyMemberSummaryVO) {
        PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
        BeanUtils.copyProperties(partyMemberSummaryVO, partyMemberSummary);
        partyMemberApprovalService.add(partyMemberSummary);
        return new BaseVo();
    }

    /**
     * 启用党员申请（草稿状态）
     *
     * @param partyMemberApprovalVO
     * @return
     */
    @PostMapping("/enabled")
    public BaseVo enabled(@Valid @RequestBody PartyMemberApprovalVO partyMemberApprovalVO) {
        partyMemberApprovalService.enabled(buildPartyMemberApproval(partyMemberApprovalVO, ActionPartyMemberEnum.ENABLED.getValue()));
        return new BaseVo();
    }

    /**
     * 停用党员申请（草稿状态）
     *
     * @param partyMemberApprovalVO
     * @return
     */
    @PostMapping("/disable")
    public BaseVo disable(@Valid @RequestBody PartyMemberApprovalVO partyMemberApprovalVO) {
        partyMemberApprovalService.disable(buildPartyMemberApproval(partyMemberApprovalVO, ActionPartyMemberEnum.DISABLE.getValue()));
        return new BaseVo();
    }

    /**
     * 提交申请
     *
     * @param param PartyMemberApproval的id
     * @return
     */
    @Transactional
    @PostMapping("/submit")
    public BaseVo submit(@RequestBody Map<String, Object> param) {
        Object id = param.get("id");
        if (id == null) {
            throw new BusinessException("审批信息的ID不能为空");
        }
        partyMemberApprovalService.submit(Long.valueOf(id.toString()), getUserId());
        return new BaseVo();
    }

    /**
     * 退回
     *
     * @param param
     * @return
     */
    @Transactional
    @PostMapping("/sendBack")
    public BaseVo sendBack(@RequestBody Map<String, Object> param) {
        Object id = param.get("id");
        Object reason = param.get("reason");
        if (id == null) {
            throw new BusinessException("审批信息的ID不能为空");
        }
        partyMemberApprovalService.sendBack(Long.valueOf(id.toString()), reason == null ? null : reason.toString(), getUserId());
        return new BaseVo();
    }

    /**
     * 通过
     *
     * @param param
     * @return
     */
    @Transactional
    @PostMapping("/pass")
    public BaseVo pass(@RequestBody Map<String, Object> param) {
        Object id = param.get("id");
        if (id == null) {
            throw new BusinessException("审批信息的ID不能为空");
        }
        PartyMemberApproval partyMemberApproval = partyMemberApprovalService.pass(Long.valueOf(id.toString()), getUserId());
        // 修改党员信息（新增、修改）
        updatePartyMember(partyMemberApproval);
        return new BaseVo();
    }

    /**
     * 【新增党员信息审批的编辑接口】
     * 修改PartyMemberApproval
     *
     * @param partyMemberSummaryVO PartyMemberApproval实体
     * @return
     */
    @Transactional
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody PartyMemberSummaryVO partyMemberSummaryVO) {
        PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
        BeanUtils.copyProperties(partyMemberSummaryVO, partyMemberSummary);
        partyMemberApprovalService.update(partyMemberSummary);
        return new BaseVo();
    }

    /**
     * 开启或停用的更改
     *
     * @param partyMemberApprovalVO
     * @return
     */
    @PostMapping("/partymember-modify")
    public BaseVo modifySwitch(@Valid @RequestBody PartyMemberApprovalVO partyMemberApprovalVO) {
        // 只能更改原因、备注和附件
        partyMemberApprovalService.updateBySwitch(partyMemberApprovalVO);
        return new BaseVo();
    }

    /**
     * 根据主键ID删除PartyMemberApproval
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        partyMemberApprovalService.delete(id);
        return new BaseVo();
    }

    /**
     * 【主要用于党员申请、启动、停用】
     * 添加记录和申请
     *
     * @param partyMemberApproval
     */
    private void addRecordAndApplay(PartyMemberApproval partyMemberApproval) {
        // 获取上级节点信息
        Organization organization = organizationService.findDirectSuperiorById(partyMemberApproval.getOrgId());
        if (organization != null) {
            // 1、添加记录
            Done done = DoneVO.buildDone(partyMemberApproval, getUserInfo(), "提交申请");
            doneService.add(done);
            // 2、添加审核任务
            Todo todo = TodoVO.buildTodo(done);
            todo.setAssigneeType(AssigneeTypeEnum.ORGANIZEID.getValue());
            todo.setAssignee(organization.getId().toString());
            todoService.add(todo);
        } else {
            throw new BusinessException("没有上一级组织架构");
        }
    }

    /**
     * 【党员申请、启动、停用】
     * 新增、修改党员信息
     *
     * @param partyMemberApproval
     * @return
     */
    private void updatePartyMember(PartyMemberApproval partyMemberApproval) {
        if (partyMemberApproval != null) {
            PartyMemberVO partyMemberVO = new PartyMemberVO();
            switch (ActionPartyMemberEnum.getByValue(partyMemberApproval.getType())) {
                case ADD:
                    // 新增党员信息
                    PartyMemberSummary partyMemberSummary = partyMemberApproval.getModification();
                    BeanUtils.copyProperties(partyMemberSummary, partyMemberVO);
                    PartyMember partyMember = partyMemberService.add(partyMemberVO);
                    // 新增军人信息
                    ExServiceman exServiceman = partyMemberApproval.getModification().getExServiceman();
                    if (exServiceman != null) {
                        exServiceman.setMemberId(partyMember.getId());
                        exServiceman.setOrgName(partyMemberApproval.getOrgName());
                        exServicemanService.add(partyMemberApproval.getModification().getExServiceman());
                    }
                    // 新增或转移党员党费
                    partyDueService.movePartyDueThisMonth(partyMember.getId(), partyMemberSummary.getOrgId());
                    break;
                case DISABLE:
                    // 停用
                    partyMemberVO.setId(partyMemberApproval.getMemberId());
                    partyMemberVO.setStatus(PMStatusEnum.DISABLE.getValue());
                    partyMemberService.update(partyMemberVO);
                    // 停用党员党费
                    partyDueService.stopMemberPartyDueThisMonth(partyMemberApproval.getMemberId());
                    break;
                case ENABLED:
                    // 启用
                    partyMemberVO.setId(partyMemberApproval.getMemberId());
                    partyMemberVO.setStatus(PMStatusEnum.NORMAL.getValue());
                    partyMemberService.update(partyMemberVO);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 回填启用、停用党员信息
     *
     * @return
     */
    private PartyMemberApproval buildPartyMemberApproval(PartyMemberApprovalVO partyMemberApprovalVO, Integer type) {
        PartyMemberApproval partyMemberApproval = new PartyMemberApproval();
        // 启用状态
        partyMemberApproval.setType(type);
        // 原因（停用才有原因）
        partyMemberApproval.setReason(ActionPartyMemberEnum.DISABLE.getValue() == type? partyMemberApprovalVO.getReason():null);
        // 党员ID
        partyMemberApproval.setMemberId(partyMemberApprovalVO.getMemberId());
        // 备注
        partyMemberApproval.setRemark(partyMemberApprovalVO.getRemark());
        // 附件
        partyMemberApproval.setAttachmentIds(partyMemberApprovalVO.getAttachmentIds());
        return partyMemberApproval;
    }

    /**
     * TODO 审核人
     *
     * @return
     */
    private Map getUserInfo() {
        Map params = new HashMap<>();
        params.put("userId", "10086");
        params.put("orgId", "72621643502977024");
        params.put("orgName", "鑫宝龙党支部");
        return params;
    }

    /**
     * TODO 回填用户ID
     *
     * @return
     */
    public Long getUserId() {
        return 10086L;
    }
}
