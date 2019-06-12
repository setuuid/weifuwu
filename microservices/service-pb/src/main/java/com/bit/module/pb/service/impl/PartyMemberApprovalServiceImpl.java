package com.bit.module.pb.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.dao.*;
import com.bit.module.pb.enums.*;
import com.bit.module.pb.service.PartyMemberApprovalService;
import com.bit.module.pb.vo.PartyMemberApprovalVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PartyMemberApproval的Service实现类
 *
 * @author codeGenerator
 */
@Slf4j
@Service("partyMemberApprovalService")
public class PartyMemberApprovalServiceImpl extends BaseService implements PartyMemberApprovalService {

    @Autowired
    private PartyMemberApprovalDao partyMemberApprovalDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private PartyMemberDao partyMemberDao;

    @Autowired
    private DoneDao doneDao;

    @Autowired
    private TodoDao todoDao;

    /**
     * 根据条件查询PartyMemberApproval
     *
     * @param partyMemberApprovalVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(PartyMemberApprovalVO partyMemberApprovalVO) {
        PageHelper.startPage(partyMemberApprovalVO.getPageNum(), partyMemberApprovalVO.getPageSize());
        List<PartyMemberApproval> list = partyMemberApprovalDao.findByConditionPage(partyMemberApprovalVO);
        PageInfo<PartyMemberApproval> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查询所有PartyMemberApproval
     *
     * @param sorter 排序字符串
     * @return
     */
    @Override
    public List<PartyMemberApproval> findAll(String sorter) {
        return partyMemberApprovalDao.findAll(sorter);
    }

    /**
     * 通过主键查询单个PartyMemberApproval
     *
     * @param id
     * @return
     */
    @Override
    public PartyMemberApproval findById(Long id) {
        return partyMemberApprovalDao.findById(id);
    }

    /**
     * 批量保存PartyMemberApproval
     *
     * @param partyMemberApprovals
     */
    @Override
    public void batchAdd(List<PartyMemberApproval> partyMemberApprovals) {
        partyMemberApprovalDao.batchAdd(partyMemberApprovals);
    }

    /**
     * 保存PartyMemberApproval
     *
     * @param partyMemberSummary
     */
    @Override
    public void add(PartyMemberSummary partyMemberSummary) {
        PartyMember partyMember = partyMemberDao.findByIdCard(partyMemberSummary.getIdCard());
        if (partyMember != null) {
            throw new BusinessException("身份证号已存在");
        }
        PartyMemberApproval partyMemberApproval = PartyMemberApproval.buildPartyMemberApproval(partyMemberSummary);
        // 回填用户信息
        UserInfo userInfo = getCurrentUserInfo();
        partyMemberApproval.setUserId(userInfo.getId());
        partyMemberApproval.setOrgId(userInfo.getPbOrgId().toString());
        partyMemberApprovalDao.add(partyMemberApproval);
    }

    @Override
    public void submit(Long id, Long userId) {
        // 获取党员审核信息
        PartyMemberApproval approval = partyMemberApprovalDao.findById(id);
        if (approval != null) {
            // 草稿、退回状态可以提交
            if (approval.getStatus() == ApprovalStatusEnum.DRAFT.getValue()
                    || approval.getStatus() == ApprovalStatusEnum.RETURN.getValue()) {
                // 更新状态
                partyMemberApprovalDao.updateByStatus(id, ApprovalStatusEnum.AUDIT.getValue());
                approval.setStatus(ApprovalStatusEnum.AUDIT.getValue());
                disposeRecords(approval, "提交申请", null);
            }
        } else {
            throw new BusinessException("申请信息不存在");
        }
    }

    @Override
    public void sendBack(Long id, String reason, Long userId) {
        // 获取党员审核信息
        PartyMemberApproval approval = partyMemberApprovalDao.findById(id);
        if (approval != null) {
            // 提交状态可以退回
            if (approval.getStatus() == ApprovalStatusEnum.AUDIT.getValue()) {
                // 更新状态
                partyMemberApprovalDao.updateByStatus(id, ApprovalStatusEnum.RETURN.getValue());
                approval.setStatus(ApprovalStatusEnum.RETURN.getValue());
                disposeRecords(approval, "退回", reason);
            } else {
                throw new BusinessException("流程状态变更，不能退回");
            }
        } else {
            throw new BusinessException("申请信息不存在");
        }
    }

    @Override
    public PartyMemberApproval pass(Long id, Long userId) {
        // 获取党员审核信息
        PartyMemberApproval approval = partyMemberApprovalDao.findById(id);
        if (approval != null) {
            // 提交状态才能通过
            if (approval.getStatus() == ApprovalStatusEnum.AUDIT.getValue()) {
                // 更新状态
                partyMemberApprovalDao.updateByStatus(id, ApprovalStatusEnum.PASSED.getValue());
                approval.setStatus(ApprovalStatusEnum.PASSED.getValue());
                disposeRecords(approval, "审批通过", null);
            }
        } else {
            throw new BusinessException("申请信息不存在");
        }
        return approval;
    }

    /**
     * 启用党员
     *
     * @param partyMemberApproval
     */
    @Override
    public PartyMemberApproval enabled(PartyMemberApproval partyMemberApproval) {
        PartyMemberApproval existEntity = partyMemberApprovalDao.findRecord(partyMemberApproval.getMemberId(), partyMemberApproval.getType());
        if (existEntity != null) {
            throw new BusinessException("申请已提交");
        }
        PartyMember partyMember = partyMemberDao.findById(partyMemberApproval.getMemberId());
        if (partyMember != null) {
            if (partyMember.getStatus() == PMStatusEnum.NORMAL.getValue()) {
                throw new BusinessException("党员已启用");
            }
            PartyMemberApproval.buildPartyMemberApproval(partyMemberApproval, partyMember);
            partyMemberApproval.setType(ActionPartyMemberEnum.ENABLED.getValue());
        }
        // 回填用户信息
        returnUserInfo(partyMemberApproval);
        partyMemberApprovalDao.add(partyMemberApproval);
        return partyMemberApproval;
    }

    /**
     * 停用党员
     *
     * @param partyMemberApproval
     */
    @Override
    public PartyMemberApproval disable(PartyMemberApproval partyMemberApproval) {
        PartyMemberApproval existEntity = partyMemberApprovalDao.findRecord(partyMemberApproval.getMemberId(), partyMemberApproval.getType());
        if (existEntity != null) {
            throw new BusinessException("该申请已提交");
        }
        PartyMember partyMember = partyMemberDao.findById(partyMemberApproval.getMemberId());
        if (partyMember != null) {
            PartyMemberApproval.buildPartyMemberApproval(partyMemberApproval, partyMember);
            partyMemberApproval.setType(ActionPartyMemberEnum.DISABLE.getValue());
        }
        // 回填用户信息
        returnUserInfo(partyMemberApproval);
        partyMemberApprovalDao.add(partyMemberApproval);
        return partyMemberApproval;
    }

    /**
     * 批量更新PartyMemberApproval
     *
     * @param partyMemberApprovals
     */
    @Override
    public void batchUpdate(List<PartyMemberApproval> partyMemberApprovals) {
        partyMemberApprovalDao.batchUpdate(partyMemberApprovals);
    }

    /**
     * 更新PartyMemberApproval
     *
     * @param partyMemberSummary
     */
    @Override
    public void update(PartyMemberSummary partyMemberSummary) {
        PartyMemberApproval item = partyMemberApprovalDao.findById(partyMemberSummary.getId());
        // 草案状态和未接受状态可以更改
        if (ApprovalStatusEnum.DRAFT.getValue() == item.getStatus()
                || ApprovalStatusEnum.RETURN.getValue() == item.getStatus()) {
            PartyMemberApproval partyMemberApproval = PartyMemberApproval.buildPartyMemberApproval(partyMemberSummary);
            partyMemberApprovalDao.update(partyMemberApproval);
        } else {
            throw new BusinessException("申请已提交，不能更改");
        }
    }

    @Override
    public void updateBySwitch(PartyMemberApprovalVO partyMemberApprovalVO) {
        PartyMemberApproval item = partyMemberApprovalDao.findById(partyMemberApprovalVO.getId());
        if (item != null) {
            if (item.getStatus() == ApprovalStatusEnum.DRAFT.getValue() ||
                    item.getStatus() == ApprovalStatusEnum.RETURN.getValue()) {
                PartyMemberApproval approval = new PartyMemberApproval();
                approval.setId(item.getId());
                approval.setReason(partyMemberApprovalVO.getReason());
                approval.setRemark(partyMemberApprovalVO.getRemark());
                approval.setAttachmentIds(partyMemberApprovalVO.getAttachmentIds());
                partyMemberApprovalDao.update(approval);
            }
        } else {
            throw new BusinessException("申请记录不存在");
        }
    }

    @Override
    public void updateByStatus(Long id, Integer status) {
        PartyMemberApproval partyMemberApproval = new PartyMemberApproval();
        partyMemberApproval.setId(id);
        partyMemberApproval.setStatus(status);
        partyMemberApprovalDao.update(partyMemberApproval);
    }

    /**
     * 删除PartyMemberApproval
     *
     * @param ids
     */
    @Override
    public void batchDelete(List<Long> ids) {
        partyMemberApprovalDao.batchDelete(ids);
    }

    @Override
    public Boolean findRecord(Long memberId, Integer type, Integer status) {
        PartyMemberApproval partyMemberApproval = new PartyMemberApproval();
        partyMemberApproval.setMemberId(memberId);
        partyMemberApproval.setType(type);
        partyMemberApproval.setStatus(ApprovalStatusEnum.DRAFT.getValue());
        partyMemberApproval = partyMemberApprovalDao.findRecord(partyMemberApproval.getMemberId(), partyMemberApproval.getType());
        return partyMemberApproval == null ? false : true;
    }

    @Override
    public PartyMemberApproval findOutreason(Long memberId) {
        return partyMemberApprovalDao.findOutreason(memberId, ActionPartyMemberEnum.DISABLE.getValue());
    }

    /**
     * 删除PartyMemberApproval
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        PartyMemberApproval partyMemberApproval = partyMemberApprovalDao.findById(id);
        Integer status = partyMemberApproval.getStatus();
        if (ApprovalStatusEnum.DRAFT.getValue() == status) {
            partyMemberApprovalDao.delete(id);
        } else {
            throw new BusinessException("该申请已提交，不能删除");
        }
    }

    /**
     * 回填记录信息
     *
     * @param approval
     * @param content
     * @param reason
     */
    private void disposeRecords(PartyMemberApproval approval, String content, String reason) {
        switch (ApprovalStatusEnum.getByValue(approval.getStatus())) {
            case AUDIT:
                // 提交
                // 添加任务
                Organization organization = organizationDao.findDirectSuperiorById(approval.getOrgId(), true);
                if (organization != null) {
                    // 1、添加记录
                    Done done = buildDone(approval, getUserInfo(), reason, content);
                    doneDao.add(done);
                    // 2、添加审核任务
                    Todo todo = buildTodo(done);
                    todo.setAssigneeType(AssigneeTypeEnum.ORGANIZEID.getValue());
                    todo.setAssignee(organization.getId().toString());
                    todo.setLastHandlerName(getUserInfo().get("orgName").toString());
                    todo.setSubmitTime(new Date());
                    todoDao.add(todo);
                } else {
                    throw new BusinessException("没有上一级组织架构");
                }
                break;
            case PASSED:
                // 通过
            case RETURN:
                // 退回
                // 1、添加记录
                Done done = buildDone(approval, getUserInfo(), reason, reason);
                doneDao.add(done);
                // 2、删除任务
                todoDao.deleteByCorrelationId(approval.getId(), TopicTypeEnum.PARTYMEMBER.getValue());
                break;
            default:
                break;
        }
    }

    private Done buildDone(PartyMemberApproval partyMemberApproval, Map<String, Object> userParam, String reason, String content){
        if (partyMemberApproval != null) {
            Done done = new Done();
            done.setOrgId(partyMemberApproval.getOrgId());
            done.setOrgName(partyMemberApproval.getOrgName());

            done.setUserId(Long.valueOf(userParam.get("userId").toString()));
            done.setReason(partyMemberApproval.getStatus() == ApprovalStatusEnum.RETURN.getValue() ? reason: null);
            done.setContent(content);
            done.setCorrelationId(partyMemberApproval.getId());
            done.setLastHandlerName(partyMemberApproval.getOrgName());

            // 主题
            done.setTopic(TopicTypeEnum.PARTYMEMBER.getValue());
            done.setAction(partyMemberApproval.getType());

            done.setSubmitTime(new Date());
            done.setHandleTime(new Date());
            return done;
        }
        return null;
    }

    /**
     * 待办信息
     * @param done
     * @return
     */
    private Todo buildTodo(Done done) {
        if (done != null) {
            Todo todo = new Todo();
            todo.setTopic(done.getTopic());
            todo.setAction(done.getAction());
            todo.setCorrelationId(done.getCorrelationId());
            todo.setSubmitTime(done.getSubmitTime());
            todo.setContent(done.getContent());
            return todo;
        }
        return null;
    }


    private void returnUserInfo(PartyMemberApproval partyMemberApproval) {
        // 回填用户信息
        UserInfo userInfo = getCurrentUserInfo();
        partyMemberApproval.setUserId(userInfo.getId());
        partyMemberApproval.setOrgId(userInfo.getPbOrgId().toString());
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
}
