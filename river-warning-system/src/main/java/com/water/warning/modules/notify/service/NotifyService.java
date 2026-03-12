package com.water.warning.modules.notify.service;

import com.water.warning.modules.notify.entity.StWarnNotify;
import com.water.warning.modules.notify.mapper.StWarnNotifyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {

    private final StWarnNotifyMapper warnNotifyMapper;

    /** 通知状态常量 */
    public static final int NOTIFY_STAT_PENDING = 0;   // 待发送
    public static final int NOTIFY_STAT_SENT = 1;      // 已发送
    public static final int NOTIFY_STAT_FAILED = 2;    // 失败

    /** 通知方式常量 */
    public static final int NOTIFY_WAY_SMS = 1;        // 短信
    public static final int NOTIFY_WAY_EMAIL = 2;      // 邮件
    public static final int NOTIFY_WAY_SYSTEM = 3;     // 系统通知

    /**
     * 异步发送通知
     */
    @Async
    public void sendNotificationAsync(StWarnNotify notify) {
        log.info("开始异步发送通知 - 通知ID: {}, 接收单位: {}, 通知方式: {}",
                notify.getNotifyId(), notify.getRecvUnitName(), notify.getNotifyWay());

        try {
            // 调用实际的通知发送逻辑
            boolean success = sendNotification(notify);

            if (success) {
                // 更新为已发送
                warnNotifyMapper.updateNotifyStat(notify.getNotifyId(), NOTIFY_STAT_SENT,
                        LocalDateTime.now(), null);
                log.info("通知发送成功 - 通知ID: {}", notify.getNotifyId());
            } else {
                // 更新为失败
                warnNotifyMapper.updateNotifyStat(notify.getNotifyId(), NOTIFY_STAT_FAILED,
                        null, "发送失败");
                log.error("通知发送失败 - 通知ID: {}", notify.getNotifyId());
            }
        } catch (Exception e) {
            log.error("通知发送异常 - 通知ID: {}, 错误: {}", notify.getNotifyId(), e.getMessage());
            warnNotifyMapper.updateNotifyStat(notify.getNotifyId(), NOTIFY_STAT_FAILED,
                    null, e.getMessage());
        }
    }

    /**
     * 发送通知（实际的通知逻辑）
     * 这里可以集成短信、邮件、App推送等第三方服务
     */
    private boolean sendNotification(StWarnNotify notify) {
        // 模拟通知发送
        log.info("发送通知 - 内容: {}", notify.getContent());

        switch (notify.getNotifyWay()) {
            case NOTIFY_WAY_SMS:
                return sendSms(notify);
            case NOTIFY_WAY_EMAIL:
                return sendEmail(notify);
            case NOTIFY_WAY_SYSTEM:
                return sendSystemNotification(notify);
            default:
                log.warn("未知的通知方式: {}", notify.getNotifyWay());
                return false;
        }
    }

    /**
     * 发送短信
     */
    private boolean sendSms(StWarnNotify notify) {
        log.info("【短信通知】发送给: {} - {}", notify.getReceiverPhone(), notify.getContent());
        // 这里调用实际的短信服务API
        // 模拟发送成功
        return true;
    }

    /**
     * 发送邮件
     */
    private boolean sendEmail(StWarnNotify notify) {
        log.info("【邮件通知】发送给: {} - {}", notify.getReceiver(), notify.getContent());
        // 这里调用实际的邮件服务API
        // 模拟发送成功
        return true;
    }

    /**
     * 发送系统通知
     */
    private boolean sendSystemNotification(StWarnNotify notify) {
        log.info("【系统通知】推送给: {} - {}", notify.getReceiver(), notify.getContent());
        // 这里调用实际的App推送服务API
        // 模拟发送成功
        return true;
    }

    /**
     * 重试发送失败的通知
     */
    public void retryFailedNotifications() {
        log.info("开始重试失败的通知");
        List<StWarnNotify> pendingNotifies = warnNotifyMapper.selectPendingNotifies();

        for (StWarnNotify notify : pendingNotifies) {
            sendNotificationAsync(notify);
        }

        log.info("重试完成，共处理 {} 条通知", pendingNotifies.size());
    }

    /**
     * 获取通知详情
     */
    public StWarnNotify getNotifyDetail(Long notifyId) {
        return warnNotifyMapper.selectById(notifyId);
    }

    /**
     * 获取预警的通知记录
     */
    public List<StWarnNotify> getNotifiesByWarnId(Long warnId) {
        return warnNotifyMapper.selectByWarnId(warnId);
    }
}
