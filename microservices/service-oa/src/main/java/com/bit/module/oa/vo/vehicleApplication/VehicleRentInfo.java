package com.bit.module.oa.vo.vehicleApplication;

import com.bit.base.exception.CheckException;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/15 17:44
 */
@Data
public class VehicleRentInfo implements Serializable {
    private Long id;
    /**
     * 企业id
     */
    private Long companyId;
    /**
     * 车牌号列表，英文逗号分隔
     */
    private String plateNos;
    /**
     * 驾驶员列表
     */
    private String driverName;
    /**
     * 驾驶员电话
     */
    private String driverPhone;

    public void checkRent() {
        if (this.getId() == null || this.getCompanyId() == null
                || StringUtils.isEmpty(this.getDriverName()) || StringUtils.isEmpty(this.getDriverPhone())) {
            throw new CheckException("用车单ID不能为空");
        }
    }
}
