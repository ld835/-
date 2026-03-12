package com.water.warning.modules.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 测站基本属性表(ST_STBPRP_B)
 * 用于存储测站的基本信息
 */
@Data
@TableName("ST_STBPRP_B")
public class StStbprpB implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测站编码
     */
    @TableId(type = IdType.INPUT)
    private String stcd;

    /**
     * 测站名称
     */
    private String stnm;

    /**
     * 行政区划
     */
    private String addvcd;

    /**
     * 水系
     */
    private String basin;

    /**
     * 河流
     */
    private String river;

    /**
     * 所在位置
     */
    private String stlc;

    /**
     * 监测类型
     */
    private String sttp;

    /**
     * 采集方式
     */
    private String dscd;

    /**
     * 管理单位
     */
    private String admauth;

    /**
     * 经度
     */
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    private BigDecimal lttd;

    /**
     * 站址
     */
    private String stak;

    /**
     * 启用时间
     */
    private LocalDateTime dt;

    /**
     * 备注
     */
    private String comments;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
