package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.enums.ApprovalStatusEnum;
import com.bit.module.pb.enums.PMStatusEnum;
import com.bit.module.pb.service.*;
import com.bit.module.pb.utils.ExcelHandler;
import com.bit.module.pb.vo.*;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * PartyMember的相关请求
 *
 * @author generator
 */
@Slf4j
@RestController
@RequestMapping(value = "/partyMember")
public class PartyMemberController {

    @Autowired
    private PartyMemberService partyMemberService;

    @Autowired
    private RelationshipTransferService relationshipTransferService;

    @Autowired
    private ExServicemanService exServicemanService;

    @Autowired
    private PartyDueService partyDueService;

    @Autowired
    private PartyMemberApprovalService partyMemberApprovalService;

    /**
     * 选择党员
     *
     * @param query
     * @return
     */
    @PostMapping("/choiceListPage")
    public BaseVo choiceListPage(@Valid @RequestBody PartyMemberQuery query) {
        PageInfo<PartyMember> pageInfo = partyMemberService.findByOrgIdsPage(query);
        return new BaseVo(pageInfo);
    }

    /**
     * 分页查询PartyMember列表
     * <p>
     * 参数：
     * 姓名：name
     * 党支部：orgId
     * 党籍状态：memberType
     * 状态：status
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PartyMemberParamsVO partyMemberParamsVO) {
        PageInfo pageInfo = new PageInfo();
        if (partyMemberParamsVO != null && partyMemberParamsVO.getStatus().size() == 1) {
            switch (PMStatusEnum.getByValue(partyMemberParamsVO.getStatus().get(0))) {
                case NORMAL:
                case DRAFT:
                    pageInfo = partyMemberService.findByConditionPage2(partyMemberParamsVO);
                    break;
                case DISABLE:
                    pageInfo = partyMemberService.findByConditionPage3(partyMemberParamsVO);
                    break;
                case EMIGRATION:
                    pageInfo = relationshipTransferService.findByConditionPage3(partyMemberParamsVO);
                    break;
                default:
                    break;
            }
            return new BaseVo(pageInfo);
        }
        // 多个条件
        pageInfo = partyMemberService.findByConditionPage2(partyMemberParamsVO);
        return new BaseVo(pageInfo);
    }

    /**
     * 获取退伍党员分页
     *
     * @param partyMemberExServicemanVO
     * @return
     */
    @PostMapping("/exServiceman/listPage")
    public BaseVo listExServicemanPage(@RequestBody PartyMemberExServicemanVO partyMemberExServicemanVO) {
        PageInfo pageInfo = partyMemberService.findExServicemanPage(partyMemberExServicemanVO);
        return new BaseVo(pageInfo);
    }

    /**
     * 根据党员ID获取信息
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        // 获取党员信息
        PartyMember partyMember = partyMemberService.findById(id);
        // 获取退伍军人信息
        ExServiceman exServiceman = exServicemanService.findByMemberId(id);
        // 转移记录
        List<RelationshipTransfer> relationshipTransfers = relationshipTransferService.findAll(null, partyMember.getIdCard(), ApprovalStatusEnum.PASSED.getValue());
        // 缴纳金额
        PartyDue partyDue = new PartyDue();
        partyDue.setMemberId(new Long(id).intValue());
        // 获取当前年份
        Calendar date = Calendar.getInstance();
        partyDue.setYear(Integer.valueOf(date.get(Calendar.YEAR)));
        List<PartyDue> partyDues = partyDueService.findPartyDueByCondition(partyDue);
        // 赋值VO
        PartyMemberDetailVO param = new PartyMemberDetailVO();
        // 如果党员是停用，获取停用原因
        if (partyMember.getStatus() == PMStatusEnum.DISABLE.getValue()) {
            PartyMemberApproval partyMemberApproval = partyMemberApprovalService.findOutreason(partyMember.getId());
            param.setApproval(partyMemberApproval);
        }
        param.setPartyMember(partyMember);
        param.setRelationshipTransfers(relationshipTransfers);
        param.setExServiceman(exServiceman);
        param.setPartyDues(partyDues);
        return new BaseVo(param);
    }

    /**
     * 根据身份证获取党员信息
     *
     * @param idCard
     * @return
     */
    @GetMapping("/findByIdCard/{idCard}")
    public BaseVo queryByIdCard(@PathVariable(value = "idCard") String idCard) {
        PartyMember partyMember = partyMemberService.findByIdCard(idCard);
        return new BaseVo(partyMember);
    }

    /**
     * 新增PartyMember
     *
     * @param partyMemberVO PartyMember实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody PartyMemberVO partyMemberVO) {
        partyMemberService.add(partyMemberVO);
        return new BaseVo();
    }

    /**
     * 批量新增PartyMember
     *
     * @param partyMembers PartyMember实体
     * @return
     */
    @PostMapping("/batchAdd")
    public BaseVo batchadd(@Valid @RequestBody List<PartyMember> partyMembers) {
        partyMemberService.batchAdd(partyMembers);
        return new BaseVo();
    }

    /**
     * 编辑 PartyMember（待完善）
     *
     * @param partyMember PartyMember实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody PartyMemberVO partyMember) {
        partyMemberService.update(partyMember);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 完善党员信息 PartyMember
     *
     * @param partyMember
     * @return
     */
    @PostMapping("/perfect")
    public BaseVo perfect(@Valid @RequestBody PartyMemberVO partyMember) {
        partyMemberService.perfect(partyMember);
        return new BaseVo();
    }

    /**
     * 根据主键ID删除PartyMember
     *
     * @param id
     * @return
     */
    @Transactional
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        partyMemberService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 导出在线党员模版
     * 或列表
     *
     * @param partyMemberParamsVO
     * @param response
     */
    @PostMapping("/export/onLine/Template")
    public void exportPartyMemberTemplate(PartyMemberParamsVO partyMemberParamsVO, HttpServletResponse response) throws IOException {
        List<PartyMemberExportVO> template = new ArrayList<>();
        PartyMemberExportVO partyMemberExportVO = new PartyMemberExportVO();
        partyMemberExportVO.setOrgId(partyMemberParamsVO.getOrgId().toString());
        template.add(partyMemberExportVO);
        ExcelHandler.exportExcelFile(response, template, PartyMemberExportVO.class, "在册党员");
    }

    /**
     * 导出在线党员模版
     * 或列表
     *
     * @param partyMemberParamsVO
     * @param response
     */
    @PostMapping("/export/onLine")
    public void exportPartyMember(PartyMemberParamsVO partyMemberParamsVO, HttpServletResponse response) throws IOException {
        List<PartyMemberExportVO> template = new ArrayList<>();
        if (partyMemberParamsVO.getOrgId() != null) {
            template = partyMemberService.findAll(partyMemberParamsVO);
        }
        ExcelHandler.exportExcelFile(response, template, PartyMemberExportVO.class, "在册党员");
    }

    /**
     * 导出停用党员模版
     * 或列表
     *
     * @param partyMember
     * @param response
     */
    @PostMapping("/export/disable")
    public void exportPartyMemberDisable(PartyMemberVO partyMember, HttpServletResponse response) throws IOException {
        List<DisablePartyExportVO> template = new ArrayList<>();
        if (partyMember.getOrgId() != null) {
            template = partyMemberService.findDisableParty(partyMember);
        }
        ExcelHandler.exportExcelFile(response, template, DisablePartyExportVO.class, "停用单元");
    }

    /**
     * 导出转出党员
     *
     * @param relationshipTransfer
     * @param response
     */
    @PostMapping("/export/transfer")
    public void exportTransferPartyMember(RelationshipTransfer relationshipTransfer, HttpServletResponse response) throws IOException {
        List<TransferPartyMemberExportVO> template = new ArrayList<>();
        if (relationshipTransfer.getFromOrgId() != null) {
            template = relationshipTransferService.findTransferPartyMember(relationshipTransfer);
        }
        ExcelHandler.exportExcelFile(response, template, TransferPartyMemberExportVO.class, "已转出党员");
    }

    /**
     * 导出退伍党员
     *
     * @param exServicemanParamVO
     * @param response
     */
    @PostMapping("/export/exServiceman")
    public void exportExServiceman(ExServicemanParamVO exServicemanParamVO, HttpServletResponse response) throws IOException {
        List<PartyMemberExportVO> template = new ArrayList<>();
        if (exServicemanParamVO.getOrgId() != null) {
            template = partyMemberService.findExServiceman(exServicemanParamVO);
        }
        ExcelHandler.exportExcelFile(response, template, PartyMemberExportVO.class, "退伍党员");
    }

    /**
     * 党员信息导入
     * TODO 临时处理
     *
     * @param excel
     * @return
     */
    @Transactional
    @PostMapping(value = "/excel/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseVo excelUpload(@RequestPart(value = "excel") MultipartFile excel) {
        List<PartyMemberExportVO> importData = ExcelHandler.importExcelFile(excel, 0, 1, PartyMemberExportVO.class);
        importData.parallelStream().forEach(partyMemberExportVO -> {
            PartyMemberVO partyMemberVO = new PartyMemberVO();
            BeanUtils.copyProperties(partyMemberExportVO, partyMemberVO);
            PartyMember partyMember = partyMemberService.add(partyMemberVO);
            ExServiceman exServiceman = new ExServiceman();
            exServiceman.setOrigin(partyMemberExportVO.getOrigin());
            exServiceman.setOriginalTroops(partyMemberExportVO.getOriginalTroops());
            exServiceman.setRetireTime(partyMemberExportVO.getRetireTime());
            exServiceman.setIsSelfEmployment(partyMemberExportVO.getIsSelfEmployment());
            exServiceman.setRelTransferTime(partyMemberExportVO.getRelTransferTime());
            if (!StringUtils.isEmpty(exServiceman)) {
                exServiceman.setOrgName(partyMemberExportVO.getOrgName());
                exServiceman.setMemberId(partyMember.getId());
                exServicemanService.add(exServiceman);
            }
        });
        return new BaseVo();
    }

    @PostMapping("/exist")
    public BaseVo existMember(@RequestBody PartyMember partyMember) {
        Boolean exist = partyMemberService.existPartyMember(partyMember.getName(), partyMember.getIdCard());
        return new BaseVo(exist);
    }

    @PostMapping("/inside/find")
    public BaseVo findInsideMemberByIdCard(@RequestBody PartyMember toQuery) {
        PartyMember partyMember = partyMemberService.findByIdCardAndInside(toQuery);
        return new BaseVo(partyMember);
    }
}
