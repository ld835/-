package com.water.warning.modules.notify.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预警通知记录表(ST_WARN_NOTIFY)
 * 用于存储预警通知发送记录
 */
@Data
@TableName("ST_WARN_NOTIFY")
public class StWarnNotify implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long notifyId;

    /**
     * 预警ID
     */
    private Long warnId;

    /**
     * 接收单位类型: 1-所属单位 2-上级单位
     */
    private Integer unitType;

    /**
     * 接收单位编码
     */
    private String recvUnit;

    /**
     * 接收单位名称
     */
    private String recvUnitName;

    /**
     * 接收人
     */
    private String receiver;

    /**
     * 接收人电话
     */
    private String receiverPhone;

    /**
     * 通知方式: 1-短信 2-邮件 3-系统通知
     */
    private Integer notifyWay;

    /**
     * 通知状态: 0-待发送 1-已发送 2-失败
     */
    private Integer notifyStat;

    /**
     * 发送时间
     */
    private LocalDateTime sendTm;

    /**
     * 发送失败原因
     */
    private String failReason;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
