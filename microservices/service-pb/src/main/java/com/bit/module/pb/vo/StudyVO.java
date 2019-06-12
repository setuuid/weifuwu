package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * StudyVO
 * @author zhangjie
 * @date 2019-1-12
 */
@Data
public class StudyVO extends BasePageVo {

    /**
     * id
     */
    private Long id;
    /**
     * 计划主题
     */
    private String theme;
    /**
     * 计划内容
     */
    private String content;
    /**
     * 附件id
     */
    private String attachmentId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建用户id
     */
    private Long createUserId;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新用户id
     */
    private Long updateUserId;
    /**
     * 是否发布  0-草稿  1-已发布
     */
    private Integer isRelease;
    /**
     * 党组织id
     */
    private Long pborgId;
    /**
     * 发布时间
     */
    private Date releaseTime;


    /**
     * 组织名
     */
    private String name;
    /**
     * 起始时间
     */
    private Date startTime;
    /**
     * 终止时间
     */
    private Date lastTime;
}
