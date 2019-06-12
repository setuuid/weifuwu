package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/7 16:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PartyMemberQuery extends BasePageVo implements Serializable {

    //columns START

    /**
     * id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 照片
     */
    private String photo;
    /**
     * 性别
     */
    private String sex;
    /**
     * 出生日期
     */
    private Date birthdate;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 民族
     */
    private String nation;
    /**
     * 入党时间
     */
    private Date joinTime;
    /**
     * 学历
     */
    private String education;
    /**
     * 1正式2预备
     */
    private Integer memberType;
    /**
     * 户籍所在派出所
     */
    private String policeStation;
    /**
     * 工作/学习单位
     */
    private String company;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 现居住地
     */
    private String address;
    /**
     * 组织id
     */
    private String orgId;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 插入时间
     */
    private Date insertTime;
    /**
     * 状态0待完善1正常2停用
     */
    private Integer status;

    /**
     * [与数据库无关]
     * 是否退伍军人
     */
    private Integer isExServiceman;
    /**
     * [与数据库无关]
     * 停用原因
     */
    private String reason;

    /**
     * 组织ID集合
     */
    private List<String> orgIds;

    //columns END
}
