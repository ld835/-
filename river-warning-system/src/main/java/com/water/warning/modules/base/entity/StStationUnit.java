package com.water.warning.modules.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理单位关联表(ST_STATION_UNIT)
 * 关联测站与管理单位
 */
@Data
@TableName("ST_STATION_UNIT")
public class StStationUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 所属单位编码（直接管理单位）
     */
    private String ownUnit;

    /**
     * 上级单位编码（主管单位）
     */
    private String parentUnit;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 单位联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 通知方式: 1-短信 2-邮件 3-系统通知
     */
    private String notifyWay;

    /**
     * 单位类型: 1-所属单位 2-上级单位
     */
    private Integer unitType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
