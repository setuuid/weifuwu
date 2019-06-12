package com.bit.module.oa.vo.vehicleApplication;

import com.bit.base.exception.CheckException;
import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/1/15 14:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleApplicationQO extends BasePageVo {
    /**
     * 申请人，用户id
     */
    private Long userId;
    /**
     * 申请单号
     */
    private String applyNo;
    /**
     * 计划开始时间
     */
    private Date planStartTime;
    /**
     * 计划结束时间
     */
    private Date planEndTime;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 申请开始时间
     */
    private Date minApplyTime;
    /**
     * 申请开始时间
     */
    private Date maxApplyTime;
    /**
     * 用车性质 0其它 1会议 2应急 3接待 4招商 5迎检
     */
    private Integer nature;
    /**
     * 用车用途 1公务用车 2租赁
     */
    private Integer usage;
    /**
     * 状态 0未派车 1已派车 2已结束 3已拒绝
     */
    private Integer status;

    public void checkMyVehiclePage() {
        checkUserId();
    }

    private VehicleApplicationQO checkUserId() {
        if (this.getUserId() == null) {
            throw new CheckException("用户ID不能为空");
        }
        return this;
    }
}
