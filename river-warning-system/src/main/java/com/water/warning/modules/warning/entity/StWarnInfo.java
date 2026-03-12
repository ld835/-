package com.water.warning.modules.warning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预警信息表(ST_WARN_INFO)
 * 用于存储预警事件信息
 */
@Data
@TableName("ST_WARN_INFO")
public class StWarnInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long warnId;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 测站名称
     */
    private String stnm;

    /**
     * 预警时间
     */
    private LocalDateTime warnTm;

    /**
     * 当前水位(m)
     */
    private BigDecimal currZ;

    /**
     * 警戒水位(m)
     */
    private BigDecimal wrz;

    /**
     * 保证水位(m)
     */
    private BigDecimal grz;

    /**
     * 预警级别: 1-警戒 2-保证
     */
    private Integer warnLv;

    /**
     * 处理状态: 0-未处理 1-处理中 2-已完成
     */
    private Integer procStat;

    /**
     * 处理人
     */
    private String handler;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理备注
     */
    private String handleRemark;

    /**
     * 预警内容
     */
    private String content;

    /**
     * 预警解除时间
     */
    private LocalDateTime resolveTm;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
