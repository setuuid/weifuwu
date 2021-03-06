package com.bit.module.oa.bean;

import java.io.Serializable;
import java.util.Date;

import com.bit.base.exception.CheckException;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * VehicleApplication
 * @author generator
 */
@Data
public class VehicleApplication implements Serializable {

	//columns START

    /**
     * id
     */
    @NotNull(message = "用车记录ID不能为空", groups = Query.class)
	private Long id;
    /**
     * 申请单号
     */	
	private String applyNo;
    /**
     * 申请人，用户id
     */
    @NotNull(message = "用户ID不能为空", groups = Apply.class)
	private Long userId;
    /**
     * 申请科室id
     */
	private Long orgId;
    /**
     * 申请科室名称
     */
	private String orgName;
    /**
     * 用车性质 0其它 1会议 2应急 3接待 4招商 5迎检
     */
    @NotNull(message = "用车性质不能为空", groups = Apply.class)
	private Integer nature;
    /**
     * 用车用途 1公务用车 2租赁
     */
    @NotNull(message = "用车用途不能为空", groups = Apply.class)
	private Integer usage;
    /**
     * 乘车人数
     */
    @NotNull(message = "乘车人数不能为空", groups = Apply.class)
	private Integer passengerNum;
    /**
     * 始发地
     */
    @NotNull(message = "始发地不能为空", groups = Apply.class)
	private String origin;
    /**
     * 目的地
     */
    @NotNull(message = "目的地不能为空", groups = Apply.class)
	private String terminal;
    /**
     * 计划开始时间
     */
    @NotNull(message = "计划开始时间不能为空", groups = Apply.class)
	private Date planStartTime;
    /**
     * 计划结束时间
     */
    @NotNull(message = "计划结束时间不能为空", groups = Apply.class)
	private Date planEndTime;
    /**
     * 用车事由
     */
    @NotNull(message = "用车事由不能为空", groups = Apply.class)
	private String applyReason;
    /**
     * 申请时间
     */	
	private Date applyTime;
    /**
     * 派车人，用户id
     */	
	private Long assignerId;
    /**
     * 派车时间
     */	
	private Date dispatchTime;
    /**
     * 开始时间
     */	
	private Date startTime;
    /**
     * 结束时间
     */	
	private Date endTime;
    /**
     * 企业id
     */	
	private Long companyId;
    /**
     * 车牌号列表，英文逗号分隔
     */	
	private String plateNos;
    /**
     * 驾驶员列表，英文逗号分隔，每项的格式为：[驾驶员名称]([驾驶员联系电话])
     */	
	private String drivers;
    /**
     * 备注
     */	
	private String remark;
    /**
     * 拒绝原因
     */	
	private String rejectReason;
    /**
     * 状态 0未派车 1已派车 2已结束 3已拒绝
     */	
	private Integer status;

	//columns END

    public interface Query {}

    public interface Apply {}

    public void checkRejectParam() {
        checkId().checkRejectReason();
    }

    private VehicleApplication checkId() {
        if (this.getId() == null) {
            throw new CheckException("用车单ID不能为空");
        }
        return this;
    }

    private VehicleApplication checkRejectReason() {
        if (StringUtils.isEmpty(this.getRejectReason())) {
            throw new CheckException("拒绝理由不能为空");
        }
        return this;
    }
}


