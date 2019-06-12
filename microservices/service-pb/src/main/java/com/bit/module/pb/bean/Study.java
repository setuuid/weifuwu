package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;

/**
 * Study实体类
 * @author zhangjie
 * @date 2019-1-11
 */
@Data
public class Study {

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


    private String name;

}
