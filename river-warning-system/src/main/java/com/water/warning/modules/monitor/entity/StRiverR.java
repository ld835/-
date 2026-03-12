package com.water.warning.modules.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 河道水情表(ST_RIVER_R)
 * 用于存储河道水文站测报的河道水情信息
 */
@Data
@TableName("ST_RIVER_R")
public class StRiverR implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测站编码
     */
    @TableId(type = IdType.INPUT)
    private String stcd;

    /**
     * 时间
     */
    @TableId(type = IdType.INPUT)
    private LocalDateTime tm;

    /**
     * 水位(m)
     */
    private BigDecimal z;

    /**
     * 流量(m³/s)
     */
    private BigDecimal q;

    /**
     * 断面过水面积(m²)
     */
    private BigDecimal xsa;

    /**
     * 断面平均流速(m/s)
     */
    private BigDecimal xsavv;

    /**
     * 断面最大流速(m/s)
     */
    private BigDecimal xsmxv;

    /**
     * 水情特征码
     */
    private String flwchrcd;

    /**
     * 水势: 4-涨 5-落 6-平
     */
    private String wptn;

    /**
     * 测流方法
     */
    private String msqmt;

    /**
     * 测积方法
     */
    private String msamt;

    /**
     * 测速方法
     */
    private String msvmt;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
