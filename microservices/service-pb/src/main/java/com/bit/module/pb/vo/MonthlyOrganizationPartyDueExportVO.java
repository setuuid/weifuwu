package com.bit.module.pb.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.bit.module.pb.bean.MonthlyPartyDue;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/3 14:19
 */
@Data
@ExcelTarget("monthlyOrganizationPartyDueExportVO")
public class MonthlyOrganizationPartyDueExportVO implements Serializable {
    @Excel(name = "序号", width = 20, orderNum = "1")
    private Integer id;

    @Excel(name = "党组织", width = 20, orderNum = "2")
    private String orgName;

    @Excel(name = "一月", width = 7, orderNum = "3")
    private Integer january;

    @Excel(name = "二月", width = 7, orderNum = "4")
    private Integer february;

    @Excel(name = "三月", width = 7, orderNum = "5")
    private Integer march;

    @Excel(name = "四月", width = 7, orderNum = "6")
    private Integer april;

    @Excel(name = "五月", width = 7, orderNum = "7")
    private Integer may;

    @Excel(name = "六月", width = 7, orderNum = "8")
    private Integer june;

    @Excel(name = "七月", width = 7, orderNum = "9")
    private Integer july;

    @Excel(name = "八月", width = 7, orderNum = "10")
    private Integer august;

    @Excel(name = "九月", width = 7, orderNum = "11")
    private Integer september;

    @Excel(name = "十月", width = 7, orderNum = "12")
    private Integer october;

    @Excel(name = "十一月", width = 7, orderNum = "13")
    private Integer november;

    @Excel(name = "十二月", width = 7, orderNum = "14")
    private Integer december;

    public void setMonthData(MonthlyPartyDue monthlyPartyDue) {
        switch (monthlyPartyDue.getMonth()) {
            case 1 :
                this.setJanuary(monthlyPartyDue.getAmount());
                break;
            case 2 :
                this.setFebruary(monthlyPartyDue.getAmount());
                break;
            case 3 :
                this.setMarch(monthlyPartyDue.getAmount());
                break;
            case 4 :
                this.setApril(monthlyPartyDue.getAmount());
                break;
            case 5 :
                this.setMay(monthlyPartyDue.getAmount());
                break;
            case 6 :
                this.setJune(monthlyPartyDue.getAmount());
                break;
            case 7 :
                this.setJuly(monthlyPartyDue.getAmount());
                break;
            case 8 :
                this.setAugust(monthlyPartyDue.getAmount());
                break;
            case 9 :
                this.setSeptember(monthlyPartyDue.getAmount());
                break;
            case 10 :
                this.setOctober(monthlyPartyDue.getAmount());
                break;
            case 11 :
                this.setNovember(monthlyPartyDue.getAmount());
                break;
            case 12 :
                this.setDecember(monthlyPartyDue.getAmount());
                break;
        }
    }
}
