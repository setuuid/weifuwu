package com.bit.module.pb.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/3 9:58
 */
@Data
@ExcelTarget("monthlyPersonalPartyDueExportVO")
public class MonthlyPersonalPartyDueExportVO implements Serializable {
    @Excel(name = "序号", width = 10, orderNum = "1")
    private Integer id;

    @Excel(name = "姓名", width = 13, orderNum = "2")
    private String memberName;

    @Excel(name = "所在党支部", width = 15, orderNum = "3")
    private String orgName;

    @Excel(name = "核算基数", width = 7, orderNum = "4")
    private Integer base;

    @Excel(name = "缴费金额", width = 7, orderNum = "5")
    private Integer amount;

    @Excel(name = "一月", width = 7, orderNum = "6")
    private Integer january;

    @Excel(name = "二月", width = 7, orderNum = "7")
    private Integer february;

    @Excel(name = "三月", width = 7, orderNum = "8")
    private Integer march;

    @Excel(name = "四月", width = 7, orderNum = "9")
    private Integer april;

    @Excel(name = "五月", width = 7, orderNum = "10")
    private Integer may;

    @Excel(name = "六月", width = 7, orderNum = "11")
    private Integer june;

    @Excel(name = "七月", width = 7, orderNum = "12")
    private Integer july;

    @Excel(name = "八月", width = 7, orderNum = "13")
    private Integer august;

    @Excel(name = "九月", width = 7, orderNum = "14")
    private Integer september;

    @Excel(name = "十月", width = 7, orderNum = "15")
    private Integer october;

    @Excel(name = "十一月", width = 7, orderNum = "16")
    private Integer november;

    @Excel(name = "十二月", width = 7, orderNum = "17")
    private Integer december;

    public void setMonthData(MonthlyPartyDueDetailVO partyDue) {
        switch (partyDue.getMonth()) {
            case 1 :
                // 核算基数和应交金额取一月份的值
                this.setJanuary(partyDue.getPaidAmount());
                this.setBase(partyDue.getBase());
                this.setAmount(partyDue.getAmount());
                break;
            case 2 :
                this.setFebruary(partyDue.getPaidAmount());
                break;
            case 3 :
                this.setMarch(partyDue.getPaidAmount());
                break;
            case 4 :
                this.setApril(partyDue.getPaidAmount());
                break;
            case 5 :
                this.setMay(partyDue.getPaidAmount());
                break;
            case 6 :
                this.setJune(partyDue.getPaidAmount());
                break;
            case 7 :
                this.setJuly(partyDue.getPaidAmount());
                break;
            case 8 :
                this.setAugust(partyDue.getPaidAmount());
                break;
            case 9 :
                this.setSeptember(partyDue.getPaidAmount());
                break;
            case 10 :
                this.setOctober(partyDue.getPaidAmount());
                break;
            case 11 :
                this.setNovember(partyDue.getPaidAmount());
                break;
            case 12 :
                this.setDecember(partyDue.getPaidAmount());
                break;
        }
    }
}
