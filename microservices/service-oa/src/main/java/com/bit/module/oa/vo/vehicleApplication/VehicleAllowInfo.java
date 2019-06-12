package com.bit.module.oa.vo.vehicleApplication;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/1/16 14:17
 */
@Data
public class VehicleAllowInfo implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 车辆列表
     */
    private List<Long> vehicleIds;
    /**
     * 驾驶员列表
     */
    private List<Long> driverIds;
    /**
     * 备注
     */
    private String remark;
    /**
     * 派车人，用户id
     * 暂定, 以后从header中取
     */
    private Long assignerId;
}
