package com.bit.module.pb.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/2 15:55
 */
@Data
@ExcelTarget("personalPartyDueExportVO")
public class PersonalPartyDueExportVO implements Serializable {
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Integer id;

    @Excel(name = "姓名", width = 15, orderNum = "2")
    private String memberName;

    @Excel(name = "身份证号", width = 20, orderNum = "3")
    private String idCard;

    @Excel(name = "所属党支部", width = 17, orderNum = "4")
    private String orgName;

    @Excel(name = "核算基数", width = 15, orderNum = "5")
    private Integer base;

    @Excel(name = "应交金额", width = 15, orderNum = "6")
    private Integer amount;

    @Excel(name = "实交金额", width = 15, orderNum = "7")
    private Integer paidAmount;

    @Excel(name = "备注", width = 15, orderNum = "8")
    private String remark;
}
