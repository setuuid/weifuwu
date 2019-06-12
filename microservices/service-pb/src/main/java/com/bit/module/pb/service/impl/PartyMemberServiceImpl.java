package com.bit.module.pb.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.bean.PartyMemberExServiceman;
import com.bit.module.pb.dao.ExServicemanDao;
import com.bit.module.pb.dao.OrganizationDao;
import com.bit.module.pb.dao.PartyMemberDao;
import com.bit.module.pb.enums.PMStatusEnum;
import com.bit.module.pb.service.PartyMemberService;
import com.bit.module.pb.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PartyMember的Service实现类
 *
 * @author codeGenerator
 */
@Service("partyMemberService")
public class PartyMemberServiceImpl extends BaseService implements PartyMemberService {

    /**
     * 限制条数
     */
    private static final int POINTSDATALIMIT = 1000;

    @Autowired
    private PartyMemberDao partyMemberDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private ExServicemanDao exServicemanDao;

    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberVO
     * @return
     */
    @Override
    public PageInfo findByConditionPage(PartyMemberQuery partyMemberVO) {
        PageHelper.startPage(partyMemberVO.getPageNum(), partyMemberVO.getPageSize());
        List<PartyMember> list = partyMemberDao.findByConditionPage(partyMemberVO);
        PageInfo<PartyMember> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<PartyMember> findByOrgIdsPage(PartyMemberQuery query) {
        if (query.getOrgId() != null) {
            List<Organization> organizationInfoVOS = organizationDao.findSubordinatesById(query.getOrgId(), true);
            List<String> orgIds = organizationInfoVOS.parallelStream().map(organizationInfoVO -> {
                return organizationInfoVO.getId();
            }).collect(Collectors.toList());
            query.setOrgIds(orgIds);
        }
        // query.setStatus(PMStatusEnum.NORMAL.getValue());
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<PartyMember> list = partyMemberDao.findByOrgIdsPage(query);
        PageInfo<PartyMember> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberVO
     * @return
     */
    @Override
    public PageInfo findByConditionPage2(PartyMemberParamsVO partyMemberVO) {
        PageHelper.startPage(partyMemberVO.getPageNum(), partyMemberVO.getPageSize());
        List<PartyMemberVO> list = partyMemberDao.findByConditionPage2(partyMemberVO);
        PageInfo<PartyMemberVO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo findByConditionPage3(PartyMemberParamsVO partyMemberVO) {
        PageHelper.startPage(partyMemberVO.getPageNum(), partyMemberVO.getPageSize());
        List<PartyMemberVO> list = partyMemberDao.findByConditionPage3(partyMemberVO);
        PageInfo<PartyMemberVO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo findExServicemanPage(PartyMemberExServicemanVO partyMemberExServicemanVO) {
        PageHelper.startPage(partyMemberExServicemanVO.getPageNum(), partyMemberExServicemanVO.getPageSize());
        List<PartyMemberExServiceman> list = partyMemberDao.findExServicemanPage(partyMemberExServicemanVO);
        PageInfo<PartyMemberExServiceman> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 查询所有PartyMember
     *
     * @param partyMember
     * @return
     */
    @Override
    public List<PartyMemberExportVO> findAll(PartyMemberParamsVO partyMember) {
        return partyMemberDao.findAll(partyMember);
    }

    @Override
    public List<PartyMemberExportVO> findExServiceman(ExServicemanParamVO exServicemanParamVO) {
        return partyMemberDao.findExServiceman(exServicemanParamVO);
    }

    @Override
    public List<DisablePartyExportVO> findDisableParty(PartyMemberVO partyMember) {
        partyMember.setStatus(PMStatusEnum.DISABLE.getValue());
        return partyMemberDao.findDisableParty(partyMember);
    }

    /**
     * 通过主键查询单个PartyMember
     *
     * @param id
     * @return
     */
    @Override
    public PartyMember findById(Long id) {
        return partyMemberDao.findById(id);
    }

    @Override
    public PartyMember findByIdCard(String idCard) {
        return partyMemberDao.findByIdCard(idCard);
    }

    @Override
    public List<PartyMember> findByIdCards(List<String> idCards) {
        return partyMemberDao.findByIdCards(idCards);
    }

    /**
     * 批量保存PartyMember
     * 根据唯一索引idCard去重
     *
     * @param partyMembers
     */
    @Override
    public void batchAdd(List<PartyMember> partyMembers) {
        int partNum = partyMembers.size() / POINTSDATALIMIT;
        partyMembers.parallelStream().forEach(partyMember -> {
            partyMember.setStatus(PartyMember.judgeStatus(partyMember));
        });
        for (int i = 0; i < partNum; i++) {
            List<PartyMember> partyMemberPart = partyMembers.stream().limit(POINTSDATALIMIT).collect(Collectors.toList());
            partyMemberDao.batchAdd(partyMemberPart);
            partyMembers.subList(0, POINTSDATALIMIT).clear();
        }
        partyMemberDao.batchAdd(partyMembers);
    }

    /**
     * 保存PartyMember
     * 根据唯一索引idCard去重
     *
     * @param partyMemberVO
     */
    @Override
    public PartyMember add(PartyMemberVO partyMemberVO) {
        try {
            // 避免赋值
            partyMemberVO.setId(null);
            PartyMember partyMember = PartyMember.buildPartyMember(partyMemberVO);
            partyMemberDao.add(partyMember);
            return partyMember;
        } catch (DuplicateKeyException exception) {
            throw new BusinessException("身份证已存在");
        }
    }

    /**
     * 批量更新PartyMember
     *
     * @param partyMembers
     */
    @Override
    public void batchUpdate(List<PartyMember> partyMembers) {
        partyMemberDao.batchUpdate(partyMembers);
    }

    /**
     * 更新PartyMember
     *
     * @param partyMember
     */
    @Override
    public void update(PartyMemberVO partyMember) {
        partyMemberDao.update(PartyMember.buildPartyMember(partyMember));
    }

    /**
     * 完善信息PartyMember
     *
     * @param partyMemberVO
     */
    @Override
    public void perfect(PartyMemberVO partyMemberVO) {
        PartyMember partyMember = partyMemberDao.findById(partyMemberVO.getId());
        if (partyMember.getStatus() != 0) {
            throw new BusinessException("党员信息已完善");
        }
        partyMemberVO.setStatus(partyMember.getStatus());
        partyMemberDao.update(PartyMember.buildPartyMember(partyMemberVO));
    }

    /**
     * 删除PartyMember
     *
     * @param ids
     */
    @Override
    public void batchDelete(List<Long> ids) {
        partyMemberDao.batchDelete(ids);
    }

    @Override
    public Boolean existPartyMember(String name, String idCard) {
        return partyMemberDao.countPartyMemberByNameAndIdCard(name, idCard) > 0;
    }

    @Override
    public PartyMember findByIdCardAndInside(PartyMember toQuery) {
        return partyMemberDao.findByIdCardAndStatus(toQuery);
    }

    /**
     * 删除PartyMember
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        partyMemberDao.delete(id);
        exServicemanDao.deleteByMemberId(id);
    }
}
