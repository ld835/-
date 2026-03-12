package com.water.warning.modules.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 河道站防洪指标表(ST_RVFCCH_B)
 * 用于存储测站的防洪指标、水文特征值等信息
 */
@Data
@TableName("ST_RVFCCH_B")
public class StRvfcchB implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测站编码
     */
    @TableId(type = IdType.INPUT)
    private String stcd;

    /**
     * 左堤高程(m)
     */
    private BigDecimal ldkel;

    /**
     * 右堤高程(m)
     */
    private BigDecimal rdkel;

    /**
     * 警戒水位(m)
     */
    private BigDecimal wrz;

    /**
     * 警戒流量(m³/s)
     */
    private BigDecimal wrq;

    /**
     * 保证水位(m)
     */
    private BigDecimal grz;

    /**
     * 保证流量(m³/s)
     */
    private BigDecimal grq;

    /**
     * 平滩流量(m³/s)
     */
    private BigDecimal flpq;

    /**
     * 实测最高水位(m)
     */
    private BigDecimal obhtz;

    /**
     * 实测最高水位出现时间
     */
    private LocalDateTime obhtztm;

    /**
     * 调查最高水位(m)
     */
    private BigDecimal ivhz;

    /**
     * 调查最高水位出现时间
     */
    private LocalDateTime ivhztm;

    /**
     * 实测最大流量(m³/s)
     */
    private BigDecimal obmxq;

    /**
     * 实测最大流量出现时间
     */
    private LocalDateTime obmxqtm;

    /**
     * 调查最大流量(m³/s)
     */
    private BigDecimal ivmxq;

    /**
     * 调查最大流量出现时间
     */
    private LocalDateTime ivmxqtm;

    /**
     * 历史最大含沙量(kg/m³)
     */
    private BigDecimal hmxsm;

    /**
     * 历史最大含沙量出现时间
     */
    private LocalDateTime hmxsmtm;

    /**
     * 历史最大断面平均流速(m/s)
     */
    private BigDecimal hmxavv;

    /**
     * 历史最大断面平均流速出现时间
     */
    private LocalDateTime hmxavvtm;

    /**
     * 历史最低水位(m)
     */
    private BigDecimal hlz;

    /**
     * 历史最低水位出现时间
     */
    private LocalDateTime hlztm;

    /**
     * 历史最小流量(m³/s)
     */
    private BigDecimal hmnq;

    /**
     * 历史最小流量出现时间
     */
    private LocalDateTime hmnqtm;

    /**
     * 高水位告警值(m)
     */
    private BigDecimal taz;

    /**
     * 大流量告警值(m³/s)
     */
    private BigDecimal taq;

    /**
     * 低水位告警值(m)
     */
    private BigDecimal laz;

    /**
     * 小流量告警值(m³/s)
     */
    private BigDecimal laq;

    /**
     * 启动预报水位标准(m)
     */
    private BigDecimal sfz;

    /**
     * 启动预报流量标准(m³/s)
     */
    private BigDecimal sfq;

    /**
     * 时间戳
     */
    private LocalDateTime moditime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
