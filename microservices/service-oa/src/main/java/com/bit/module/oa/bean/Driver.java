package com.bit.module.oa.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Driver
 * @author generator
 */
@Data
public class Driver implements Serializable {

	//columns START

    /**
     * id
     */
    @NotNull(message = "驾驶员id不能为空", groups = Update.class)
	private Long id;
    /**
     * 姓名
     */
    @NotNull(message = "驾驶员姓名不能为空", groups = {Add.class})
	private String name;
    /**
     * 年龄
     */
	private Integer age;
    /**
     * 性别
     */
	private String sex;
    /**
     * 身高，单位cm
     */
	private Integer height;
    /**
     * 体重，单位kg
     */
	private String weight;
    /**
     * 健康状况
     */
	private String health;
    /**
     * 左眼视力
     */
	private String leftVision;
    /**
     * 右眼视力
     */
	private String rightVision;
    /**
     * 驾照等级
     */
    @NotNull(message = "驾驶员驾照等级不能为空", groups = {Add.class})
    private String drivingClass;
    /**
     * 驾龄 1一年及一年以下 2一到三年 3三到五年 4五到十年 5十年以上
     */
    @NotNull(message = "驾驶员驾龄不能为空", groups = {Add.class})
	private Integer drivingExperience;
    /**
     * 联系电话
     */
    @NotNull(message = "驾驶员联系方式不能为空", groups = Add.class)
	private String mobile;
    /**
     * 驾照正面照
     */
    @NotNull(message = "驾驶员驾照正面照不能为空", groups = Add.class)
	private String drivingLicenseFrontSide;
    /**
     * 驾照反面照
     */
    @NotNull(message = "驾驶员驾照反面照不能为空", groups = Add.class)
	private String drivingLicenseBackSide;
    /**
     * 身份证正面照
     */
    @NotNull(message = "驾驶员身份证正面照不能为空", groups = Add.class)
	private String idCardFrontSide;
    /**
     * 身份证反面照
     */
    @NotNull(message = "驾驶员身份证反面照不能为空", groups = Add.class)
	private String idCardBackSide;
    /**
     * 状态 0停用 1启用
     */
	private Integer status;

	//columns END

    public interface Add {}

    public interface Update {}
}


