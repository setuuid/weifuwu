package com.bit.module.pb.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.dao.*;
import com.bit.module.pb.enums.*;
import com.bit.module.pb.service.ExServicemanService;
import com.bit.module.pb.service.RelationshipTransferService;
import com.bit.module.pb.vo.PartyMemberParamsVO;
import com.bit.module.pb.vo.RelationshipTransferParamsVO;
import com.bit.module.pb.vo.RelationshipTransferVO;
import com.bit.module.pb.vo.TransferPartyMemberExportVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * RelationshipTransfer的Service实现类
 *
 * @author codeGenerator
 */
@Service("relationshipTransferService")
public class RelationshipTransferServiceImpl extends BaseService implements RelationshipTransferService {

    @Autowired
    private RelationshipTransferDao relationshipTransferDao;

    @Autowired
    private PartyMemberDao partyMemberDao;

    @Autowired
    private ExServicemanService exServicemanService;

    @Autowired
    private TodoDao todoDao;

    @Autowired
    private DoneDao doneDao;

    @Autowired
    private OrganizationDao organizationDao;

    /**
     * 根据条件查询RelationshipTransfer
     *
     * @param relationshipTransferVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(RelationshipTransferVO relationshipTransferVO) {
        PageHelper.startPage(relationshipTransferVO.getPageNum(), relationshipTransferVO.getPageSize());
        List<RelationshipTransfer> list = relationshipTransferDao.findByConditionPage(relationshipTransferVO);
        PageInfo<RelationshipTransfer> pageInfo = new PageInfo<RelationshipTransfer>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public PageInfo findByOutListPage(RelationshipTransferVO relationshipTransferVO) {
        PageHelper.startPage(relationshipTransferVO.getPageNum(), relationshipTransferVO.getPageSize());
        List<RelationshipTransfer> list = relationshipTransferDao.findByOutListPage(relationshipTransferVO);
        PageInfo<RelationshipTransfer> pageInfo = new PageInfo<RelationshipTransfer>(list);
        return pageInfo;
    }

    @Override
    public PageInfo findByReceptionPage(RelationshipTransferVO relationshipTransferVO) {
        PageHelper.startPage(relationshipTransferVO.getPageNum(), relationshipTransferVO.getPageSize());
        List<RelationshipTransfer> list = relationshipTransferDao.findByReceptionPage(relationshipTransferVO);
        list.parallelStream().forEach(relationshipTransfer -> {
            if (relationshipTransfer.getModification() != null) {
                if (relationshipTransfer.getModification().getExServiceman() != null) {
                    relationshipTransfer.setIsExServiceman(1);
                } else {
                    relationshipTransfer.setIsExServiceman(0);
                }
            }
        });
        PageInfo<RelationshipTransfer> pageInfo = new PageInfo<RelationshipTransfer>(list);
        return pageInfo;
    }

    @Override
    public PageInfo findByConditionPage3(PartyMemberParamsVO partyMemberVO) {
        PageHelper.startPage(partyMemberVO.getPageNum(), partyMemberVO.getPageSize());
        List<RelationshipTransfer> list = relationshipTransferDao.findByConditionPage3(partyMemberVO);
        PageInfo<RelationshipTransfer> pageInfo = new PageInfo<RelationshipTransfer>(list);
        return pageInfo;
    }

    @Override
    public PageInfo findByTransferPage(RelationshipTransferParamsVO paramsVO) {
        PageHelper.startPage(paramsVO.getPageNum(), paramsVO.getPageSize());
        List<RelationshipTransfer> list = new ArrayList<>();
        switch (TransferTypeMenu.getByValue(paramsVO.getTransferType())) {
            case BETWEEN:
                list = relationshipTransferDao.findByBetweenPage(paramsVO);
                break;
            case OUT:
                list = relationshipTransferDao.findByOutPage(paramsVO);
                break;
            case IN:
                list = relationshipTransferDao.findByInPage(paramsVO);
                break;
            default:
                break;
        }
        PageInfo<RelationshipTransfer> pageInfo = new PageInfo<RelationshipTransfer>(list);
        return pageInfo;
    }

    /**
     * 查询所有RelationshipTransfer
     *
     * @param memberId
     * @return
     */
    @Override
    public List<RelationshipTransfer> findAll(Long memberId, String idCard, Integer status) {
        return relationshipTransferDao.findAll(memberId, idCard, status);
    }

    /**
     * 通过主键查询单个RelationshipTransfer
     *
     * @param id
     * @return
     */
    @Override
    public RelationshipTransfer findById(Long id) {
        return relationshipTransferDao.findById(id);
    }

    @Override
    public List<RelationshipTransfer> findByMemberId(Long memberId) {
        RelationshipTransferVO params = new RelationshipTransferVO();
        params.setMemberId(memberId);
        params.setStatus(ApprovalStatusEnum.PASSED.getValue());
        return relationshipTransferDao.findByConditionPage(params);
    }

    /**
     * 批量保存RelationshipTransfer
     *
     * @param relationshipTransfers
     */
    @Override
    public void batchAdd(List<RelationshipTransfer> relationshipTransfers) {
        relationshipTransferDao.batchAdd(relationshipTransfers);
    }

    /**
     * 镇内转移
     * 保存RelationshipTransfer
     *
     * @param relationshipTransferVO
     */
    @Override
    public void add(RelationshipTransferVO relationshipTransferVO) {
        // 判断是否已申请
        RelationshipTransfer entity = relationshipTransferDao.findRecord(relationshipTransferVO.getMemberId(), ApprovalStatusEnum.PASSED.getValue());
        if (entity != null) {
            throw new BusinessException("该党员已存在申请记录");
        }
        // 回填党员信息
        Organization organization = organizationDao.findById(relationshipTransferVO.getToOrgId());
        if (organization != null) {
            relationshipTransferVO.setToOrgName(organization.getName());
        }
        PartyMember partyMember = partyMemberDao.findById(relationshipTransferVO.getMemberId());
        RelationshipTransfer relationshipTransfer = RelationshipTransfer.buildRelationshipTransfer(relationshipTransferVO, partyMember);
        PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
        BeanUtils.copyProperties(partyMember, partyMemberSummary);
        relationshipTransfer.setModification(partyMemberSummary);
        relationshipTransferDao.add(relationshipTransfer);
    }

    @Override
    public void reception(PartyMemberSummary partyMemberSummary) {
        RelationshipTransfer relationshipTransfer = RelationshipTransfer.buildRelationshipTransfer(partyMemberSummary);
        relationshipTransferDao.add(relationshipTransfer);
    }

    /**
     * 批量更新RelationshipTransfer
     *
     * @param relationshipTransfers
     */
    @Override
    public void batchUpdate(List<RelationshipTransfer> relationshipTransfers) {
        relationshipTransferDao.batchUpdate(relationshipTransfers);
    }

    /**
     * 更新RelationshipTransfer
     *
     * @param relationshipTransferVO
     */
    @Override
    public void update(RelationshipTransferVO relationshipTransferVO) {
        RelationshipTransfer relationshipTransfer = relationshipTransferDao.findById(relationshipTransferVO.getId());
        if (relationshipTransfer != null) {
            if (relationshipTransfer.getStatus() == ApprovalStatusEnum.DRAFT.getValue()
                    || relationshipTransfer.getStatus() == ApprovalStatusEnum.RETURN.getValue()) {
                PartyMember partyMember = partyMemberDao.findById(relationshipTransferVO.getMemberId());
                relationshipTransfer = RelationshipTransfer.buildRelationshipTransfer(relationshipTransferVO, partyMember);
                PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
                BeanUtils.copyProperties(partyMember, partyMemberSummary);
                relationshipTransfer.setModification(partyMemberSummary);
                relationshipTransferDao.update(relationshipTransfer);
            } else {
                throw new BusinessException("申请已提交，不能更改");
            }
        } else {
            throw new BusinessException("申请记录不存在");
        }
    }

    /**
     * 更改镇外党员转入信息
     *
     * @param partyMemberSummary
     */
    @Override
    public void updateReception(PartyMemberSummary partyMemberSummary) {
        RelationshipTransfer relationshipTransfer = relationshipTransferDao.findById(partyMemberSummary.getId());
        if (relationshipTransfer != null) {
            if (relationshipTransfer.getStatus() == ApprovalStatusEnum.DRAFT.getValue()
                    || relationshipTransfer.getStatus() == ApprovalStatusEnum.RETURN.getValue()) {
                relationshipTransfer = RelationshipTransfer.buildRelationshipTransfer(partyMemberSummary);
                relationshipTransferDao.update(relationshipTransfer);
            } else {
                throw new BusinessException("申请已提交，不能更改");
            }
        } else {
            throw new BusinessException("申请记录不存在");
        }
    }

    @Override
    public void updateFlow(Long id, Integer status, String reason) {
        RelationshipTransfer relationshipTransfer = relationshipTransferDao.findById(id);
        if (relationshipTransfer == null) {
            throw new BusinessException("申请记录不存在");
        }
        switch (ApprovalStatusEnum.getByValue(status)) {
            case AUDIT:
                // 审核中（提交）
                // '草稿'、‘退回’状态可以修改成功
                audit(relationshipTransfer, reason);
                break;
            case RETURN:
                // 退回
                // ‘审核中’的状态可以修改
                sentBack(relationshipTransfer, reason);
                break;
            case PASSED:
                // 通过[‘审核中’的状态可以修改]
                passed(relationshipTransfer);
                break;
            case RECEIVE:
                // 待接收
                receive(relationshipTransfer);
                break;
            default:
                break;
        }
    }

    /**
     * 删除RelationshipTransfer
     *
     * @param ids
     */
    @Override
    public void batchDelete(List<Long> ids) {
        relationshipTransferDao.batchDelete(ids);
    }

    @Override
    public List<TransferPartyMemberExportVO> findTransferPartyMember(RelationshipTransfer relationshipTransfer) {
        relationshipTransfer.setStatus(ApprovalStatusEnum.PASSED.getValue());
        return relationshipTransferDao.findTransferPartyMember(relationshipTransfer);
    }

    /**
     * 删除RelationshipTransfer
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        RelationshipTransfer relationshipTransfer = relationshipTransferDao.findById(id);
        if (relationshipTransfer != null) {
            if (relationshipTransfer.getStatus() == ApprovalStatusEnum.DRAFT.getValue()) {
                relationshipTransferDao.delete(id);
            } else {
                throw new BusinessException("申请已提交，不能删除");
            }
        } else {
            throw new BusinessException("申请不存在");
        }
    }

    /**
     * 添加记录
     *
     * @param relationshipTransfer
     * @param reason
     */
    private void addDone(RelationshipTransfer relationshipTransfer, String reason, Map userInfo) {
        Done done = new Done();
        done.setTopic(TopicTypeEnum.TRANSFER.getValue());
        done.setAction(ActionTransferEnum.TRANSFER.getValue());
        done.setCorrelationId(relationshipTransfer.getId());
        done.setUserId(Long.valueOf(userInfo.get("userId").toString()));
        done.setOrgId(userInfo.get("orgId").toString());
        done.setOrgName(userInfo.get("orgName").toString());
        done.setReason(relationshipTransfer.getStatus() == ApprovalStatusEnum.RETURN.getValue() ? reason : null);
        done.setContent(reason);
        done.setAttachmentIds(relationshipTransfer.getAttachmentIds());
        done.setHandleTime(new Date());
        done.setSubmitTime(new Date());
        doneDao.add(done);
    }

    /**
     * 审核（表单提交）
     *
     * @param relationshipTransfer
     */
    private void audit(RelationshipTransfer relationshipTransfer, String reason) {
        // 更新bean
        RelationshipTransfer item = new RelationshipTransfer();
        // 新增待办
        Todo todo = new Todo();
        // 审核中（提交）['草稿'、‘退回’状态可以修改成功]
        if (relationshipTransfer.getStatus() == ApprovalStatusEnum.DRAFT.getValue()
                || relationshipTransfer.getStatus() == ApprovalStatusEnum.RETURN.getValue()) {
            // 回填状态
            item.setId(relationshipTransfer.getId());
            item.setStatus(ApprovalStatusEnum.AUDIT.getValue());
            // 回填待办
            todo.setContent(reason);
            todo.setTopic(TopicTypeEnum.TRANSFER.getValue());
            if (relationshipTransfer.getToOrgId() == null) {
                // 转出镇外
                todo.setAction(ActionTransferEnum.OUTSIDE.getValue());
            } else if (relationshipTransfer.getFromOrgId() == null) {
                // 镇外转入
                todo.setAction(ActionTransferEnum.INTOWN.getValue());
            } else {
                // 转入
                todo.setAction(ActionTransferEnum.SHIFTTO.getValue());
            }
            todo.setCorrelationId(relationshipTransfer.getId());
            todo.setAssigneeType(AssigneeTypeEnum.ORGANIZEID.getValue());
            // 受理ID
            todo.setAssignee(relationshipTransfer.getToOrgId());
            if (relationshipTransfer.getToOrgId() == null) {
                // 如果是镇内转镇外的，由于没有目标ID，需要获取下一级ID
                Organization organization = organizationDao.findDirectSuperiorById(relationshipTransfer.getFromOrgId(), true);
                if (organization != null) {
                    todo.setAssignee(organization.getId().toString());
                } else {
                    throw new BusinessException("没有下一级组织架构");
                }
            }
            todo.setSubmitTime(new Date());
            relationshipTransferDao.update(item);
            todoDao.add(todo);
            // 添加审核记录
            addDone(relationshipTransfer, "提交申请", getUserInfo());
        }
    }

    /**
     * 回退
     *
     * @param relationshipTransfer
     * @param reason
     */
    private void sentBack(RelationshipTransfer relationshipTransfer, String reason) {
        // 更新bean
        RelationshipTransfer item = new RelationshipTransfer();
        // 新增待办
        if (relationshipTransfer.getStatus() == ApprovalStatusEnum.AUDIT.getValue()) {
            // 回填状态
            item.setId(relationshipTransfer.getId());
            item.setStatus(ApprovalStatusEnum.RETURN.getValue());
            relationshipTransferDao.update(item);
            // 删除待办信息
            todoDao.deleteByCorrelationId(relationshipTransfer.getId(), TopicTypeEnum.TRANSFER.getValue());
            // 添加记录
            relationshipTransfer.setStatus(ApprovalStatusEnum.RETURN.getValue());
            addDone(relationshipTransfer, reason, getUserInfo());
        }
    }

    /**
     * 审核通过
     *
     * @param relationshipTransfer
     */
    private void passed(RelationshipTransfer relationshipTransfer) {
        /**
         * 镇内互转的或镇外转镇内的党员，
         * 待接收为最后一个流程，直接改为已通过；
         * 如果有镇内往外转的，一下个状态为待接收
         */
        // 更新bean
        RelationshipTransfer item = new RelationshipTransfer();
        if (relationshipTransfer.getToOrgId() == null) {
            // 镇内转镇外，需要下一级审核
            item.setId(relationshipTransfer.getId());
            item.setStatus(ApprovalStatusEnum.RECEIVE.getValue());
            // 获取上级信息，转变代办人
            Organization organization = organizationDao.findDirectSuperiorById(relationshipTransfer.getFromOrgId(), true);
            if (organization != null) {
                // 新增待办
                Todo todo = todoDao.findByCorrelationId(relationshipTransfer.getId(), TopicTypeEnum.TRANSFER.getValue());
                todo.setContent("确认转出");
                todo.setAssignee(organization.getId().toString());
                relationshipTransferDao.update(item);
                // 将审核人改为下一节点（第一级）
                todoDao.update(todo);
            } else {
                throw new BusinessException("没有下一级组织架构");
            }
        } else {
            // 镇内、镇外转镇内，直接通过审核
            item.setId(relationshipTransfer.getId());
            item.setStatus(ApprovalStatusEnum.PASSED.getValue());
            item.setInTime(new Date());
            relationshipTransferDao.update(item);
            // 删除待办
            todoDao.deleteByCorrelationId(relationshipTransfer.getId(), TopicTypeEnum.TRANSFER.getValue());
            // 添加党员信息
            addOrUpdatePartyMember(relationshipTransfer);
        }
        // 添加审核记录
        addDone(relationshipTransfer, "审核通过", getUserInfo());
    }

    /**
     * 待接收（审核通过）
     *
     * @param relationshipTransfer
     */
    private void receive(RelationshipTransfer relationshipTransfer) {
        // 更新bean
        RelationshipTransfer item = new RelationshipTransfer();
        if (relationshipTransfer.getStatus() == ApprovalStatusEnum.RECEIVE.getValue()) {
            item.setId(relationshipTransfer.getId());
            item.setStatus(ApprovalStatusEnum.PASSED.getValue());
            item.setInTime(new Date());
            relationshipTransferDao.update(item);
            // 删除待办
            todoDao.deleteByCorrelationId(relationshipTransfer.getId(), TopicTypeEnum.TRANSFER.getValue());
            // 添加审核记录
            addDone(relationshipTransfer, "审核通过", getUserInfo());
            // 修改党员信息，将党员orgId置空
            partyMemberDao.updateByTransfer(relationshipTransfer.getId(), null);
        } else {
            throw new BusinessException("流程状态不正确");
        }
    }

    /**
     * 新增或修改党员信息
     *
     * @param relationshipTransfer
     */
    private void addOrUpdatePartyMember(RelationshipTransfer relationshipTransfer) {
        // 修改党员信息：1：根据身份证获取党员信息，有则改，没有则新增；2：修改党员信息
        PartyMember partyMember = partyMemberDao.findByIdCard(relationshipTransfer.getModification().getIdCard());
        if (partyMember == null) {
            partyMember = new PartyMember();
            PartyMemberSummary partyMemberSummary = relationshipTransfer.getModification();
            BeanUtils.copyProperties(partyMemberSummary, partyMember);
            partyMember.setOrgId(relationshipTransfer.getToOrgId());
            partyMember.setInsertTime(new Date());
            partyMember.setStatus(PMStatusEnum.NORMAL.getValue());
            partyMemberDao.add(partyMember);
            // 退伍信息
            ExServiceman exServiceman = relationshipTransfer.getModification().getExServiceman();
            if (exServiceman != null) {
                exServiceman.setMemberId(partyMember.getId());
                exServicemanService.add(exServiceman);
            }
        } else {
            partyMember.setOrgId(relationshipTransfer.getToOrgId());
            partyMemberDao.update(partyMember);
        }
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
