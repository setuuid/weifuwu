package com.bit.module.oa.vo.vehicleLog;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/1/16 17:21
 */
@Data
public class VehicleLogDetailVO implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 车辆id
     */
    private Long vehicleId;
    /**
     * 车牌号
     */
    private String plateNo;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 用车申请记录id
     */
    private Long applicationId;
}
