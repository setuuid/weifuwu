package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Dict
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictVO extends BasePageVo{

    /**
     * id
     */
    private Long id;
    /**
     * 模块/表单/类型
     */
    private String module;
    /**
     * key
     */
    private String dictCode;
    /**
     * 字符值
     */
    private String dictName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String remark;
}


