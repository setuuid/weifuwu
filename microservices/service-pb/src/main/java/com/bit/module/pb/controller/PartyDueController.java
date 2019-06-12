package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.bean.PartyDue;
import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.enums.PMStatusEnum;
import com.bit.module.pb.service.OrganizationService;
import com.bit.module.pb.service.PartyDueService;
import com.bit.module.pb.service.PartyMemberService;
import com.bit.module.pb.utils.ExcelHandler;
import com.bit.module.pb.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * PartyDue的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/partyDue")
public class PartyDueController {
	private static final Logger logger = LoggerFactory.getLogger(PartyDueController.class);
	@Autowired
	private PartyDueService partyDueService;

	@Autowired
    private PartyMemberService partyMemberService;

	@Autowired
    private OrganizationService organizationService;

    @Value("${party.due.charge.time}")
    private Integer chargeAt;

    /**
     * 分页查询PartyDue列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PartyDueVO partyDueVO) {
    	//分页对象，前台传递的包含查询的参数
        return partyDueService.findByOrgConditionPage(partyDueVO);
    }

    /**
     * 新增PartyDue
     *
     * @param partyDue PartyDue实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody PartyDue partyDue) {
        partyDueService.add(partyDue);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改PartyDue
     *
     * @param partyDue PartyDue实体
     * @return
     */
    @PostMapping("/amount/modify")
    public BaseVo modifyPartyDue(@Valid @RequestBody PartyDue partyDue) {
        return partyDueService.updateAmountById(partyDue);
    }

    @PostMapping("/monthly/listPage")
    public BaseVo listPageMonthly(@RequestBody PartyDueVO partyDueVO) {
        return partyDueService.findPersonalMonthlyPartyDue(partyDueVO);
    }

    /**
     * 根据主键ID删除PartyDue
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        partyDueService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    @GetMapping("/generate")
    public BaseVo generate() {
        partyDueService.createPartyDueEveryMonth();
        return new BaseVo();
    }

    @PostMapping("/personal/export")
    public void exportPartyDue(@RequestParam(name = "year") Integer year,
                               @RequestParam(name = "month") Integer month,
                               @RequestParam(name = "orgId", required = false) String orgId,
                               HttpServletResponse response) {
        try {
            List<PersonalPartyDueExportVO> personalPartyDueExportVOList = partyDueService
                    .findExportPersonalPartyDue(year, month, orgId);
            AtomicInteger num = new AtomicInteger(0);
            personalPartyDueExportVOList.forEach(due -> due.setId(num.addAndGet(1)));
            ExcelHandler.exportExcelFile(response, personalPartyDueExportVOList, PersonalPartyDueExportVO.class, "党员个人党费交纳情况表");
        } catch (IOException e) {
            logger.error("党费导出异常 : {}", e);
        }
    }

    @PostMapping("/personal/export/template")
    public void exportPartyDueTemplate(@RequestParam(name = "orgId", required = false) String orgId,
                                       HttpServletResponse response) {
        try {
            LocalDate now = LocalDate.now();
            List<PersonalPartyDueExportVO> template = partyDueService
                    .findExportPersonalPartyDue(now.getYear(), now.getMonthValue(), orgId);
            AtomicInteger num = new AtomicInteger(0);
            template.forEach(due -> {
                due.setId(num.addAndGet(1));
                due.setAmount(null);
                due.setBase(null);
                due.setRemark(null);
                due.setPaidAmount(null);
            });
            ExcelHandler.exportExcelFile(response, template, PersonalPartyDueExportVO.class, "党员个人党费交纳情况模板表");
        } catch (IOException e) {
            logger.error("模板导出异常 : {}", e);
        }
    }

    @PostMapping("/monthly/export")
    public void exportPersonalMonthlyPartyDue(@RequestParam(name = "year") Integer year,
                                              @RequestParam(name = "orgId", required = false) String orgId,
                                              HttpServletResponse response) {
        try {
            List<PersonalMonthlyPartyDueVO> personalMonthlyPartyDueVOS = partyDueService
                    .exportPersonalMonthlyPartyDue(year, orgId);
            List<MonthlyPersonalPartyDueExportVO> exportData = determineMonthlyPersonalExportData(personalMonthlyPartyDueVOS);
            ExcelHandler.exportExcelFile(response, exportData, MonthlyPersonalPartyDueExportVO.class, "党员缴纳党费明细表");
        } catch (Exception e) {
            logger.error("党费导出异常 : {}", e);
        }
    }

    @PostMapping(value = "/excel/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseVo excelUpload(@RequestPart(value = "excel") MultipartFile excel) {
        /*LocalDate now = LocalDate.now();
        if (chargeAt < now.getDayOfMonth()) {
            logger.warn("无法在 {} 月 {} 日之后, 编辑当前记录", now.getMonthValue(), chargeAt);
            return new BaseVo(-1, "无法修改已归档的党费数据");
        }*/
        List<PartyDueImportVO> importData = ExcelHandler.importExcelFile(excel, 0, 1, PartyDueImportVO.class);
        List<PartyDueImportVO> collect = filterEmptyIdCard(importData);
        // 身份证不能为空
        if (CollectionUtils.isNotEmpty(collect)) {
            return raiseEmptyIdCardSerialNo(collect);
        }
        List<String> idCardList = importData.stream().map(PartyDueImportVO::getIdCard).collect(Collectors.toList());
        List<PartyMember> members = partyMemberService.findByIdCards(idCardList);
        List<PartyMember> notNormalStatusMember = findNotNormalStatusMember(members);
        List<PartyMember> insertPartyDueMember = new ArrayList<PartyMember>(CollectionUtils.subtract(members, notNormalStatusMember));
        if (CollectionUtils.isEmpty(insertPartyDueMember)) {
            return new BaseVo();
        }
        List<PartyDue> partyDueImportData = obtainInsertPartyDueData(importData, insertPartyDueMember);
        partyDueService.batchUpdate(partyDueImportData);
        return new BaseVo();
    }


    // 是否有非正常状态的党员
    private List<PartyMember> findNotNormalStatusMember(List<PartyMember> members) {
        // todo 之后加上判断是否党员是否属于当前管理员
        return members.stream().filter(partyMember ->
                partyMember.getStatus() != PMStatusEnum.NORMAL.getValue()
                && partyMember.getStatus() != PMStatusEnum.DRAFT.getValue())
                .collect(Collectors.toList());
    }

    private List<PartyDue> obtainInsertPartyDueData(List<PartyDueImportVO> importData, List<PartyMember> members) {
        List<Organization> allOrganization = organizationService.findAll(null);
        Date insertTime = new Date();
        Map<String, PartyMember> memberMap = members.stream()
                .collect(Collectors.toMap(PartyMember::getIdCard, partyMember -> partyMember));
        Map<String, Organization> organizationMap = allOrganization.stream()
                .collect(Collectors.toMap(Organization::getId, organization -> organization));
        return importData.stream().filter(due -> memberMap.containsKey(due.getIdCard())).map(data -> {
            PartyMember partyMember = memberMap.get(data.getIdCard());
            PartyDue partyDue = new PartyDue();
            partyDue.setMemberId(Integer.valueOf(partyMember.getId().toString()));
            partyDue.setOrgId(partyMember.getOrgId());
            partyDue.setOrgName(organizationMap.get(partyMember.getOrgId()).getName());
            partyDue.setBase(data.getBase());
            partyDue.setAmount(data.getAmount());
            partyDue.setPaidAmount(data.getPaidAmount());
            partyDue.setRemark(data.getRemark());
            partyDue.setInsertTime(insertTime);
            return partyDue;
        }).collect(Collectors.toList());
    }

    private BaseVo raiseEmptyIdCardSerialNo(List<PartyDueImportVO> collect) {
        List<Integer> emptyIdCardNoList = collect.stream().map(PartyDueImportVO::getId).collect(Collectors.toList());
        BaseVo baseVo = new BaseVo();
        baseVo.setMsg("缺失身份证号序号：" + emptyIdCardNoList);
        baseVo.setCode(-1);
        return baseVo;
    }

    private List<PartyDueImportVO> filterEmptyIdCard(List<PartyDueImportVO> importData) {
        return importData.stream().filter(data -> StringUtils.isEmpty(data.getIdCard()))
                .collect(Collectors.toList());
    }

    private List<MonthlyPersonalPartyDueExportVO> determineMonthlyPersonalExportData(
            List<PersonalMonthlyPartyDueVO> personalMonthlyPartyDueVOS) {
        List<MonthlyPersonalPartyDueExportVO> monthlyPersonalPartyDueExportVOS = new ArrayList<>();
        AtomicInteger num = new AtomicInteger(0);
        personalMonthlyPartyDueVOS.forEach(monthlyPartyDueVO -> {
            MonthlyPersonalPartyDueExportVO monthlyPersonalPartyDueExportVO = new MonthlyPersonalPartyDueExportVO();
            monthlyPersonalPartyDueExportVO.setId(num.addAndGet(1));
            monthlyPersonalPartyDueExportVO.setMemberName(monthlyPartyDueVO.getName());
            monthlyPersonalPartyDueExportVO.setOrgName(monthlyPartyDueVO.getOrgName());
            setExportMonthlyData(monthlyPartyDueVO, monthlyPersonalPartyDueExportVO);
            // 防止因为一月没有数据导致数据为空
            if (CollectionUtils.isNotEmpty(monthlyPartyDueVO.getPartyDues()) && (monthlyPersonalPartyDueExportVO.getBase() == null || monthlyPersonalPartyDueExportVO.getAmount() == null)) {
                monthlyPersonalPartyDueExportVO.setBase(monthlyPartyDueVO.getPartyDues().get(0).getBase());
                monthlyPersonalPartyDueExportVO.setAmount(monthlyPartyDueVO.getPartyDues().get(0).getAmount());
            }
            monthlyPersonalPartyDueExportVOS.add(monthlyPersonalPartyDueExportVO);
        });
        return monthlyPersonalPartyDueExportVOS;
    }

    private void setExportMonthlyData(PersonalMonthlyPartyDueVO monthlyPartyDueVO,
                                      MonthlyPersonalPartyDueExportVO monthlyPersonalPartyDueExportVO) {
        if (CollectionUtils.isEmpty(monthlyPartyDueVO.getPartyDues())) {
            return;
        }
        for (MonthlyPartyDueDetailVO partyDue : monthlyPartyDueVO.getPartyDues()) {
            matchMonthData(partyDue, monthlyPersonalPartyDueExportVO);
        }
    }

    private void matchMonthData(MonthlyPartyDueDetailVO partyDue,
                                MonthlyPersonalPartyDueExportVO monthlyPersonalPartyDueExportVO) {
        monthlyPersonalPartyDueExportVO.setMonthData(partyDue);
    }
}
