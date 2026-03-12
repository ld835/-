package com.water.warning.job;

import com.water.warning.modules.monitor.entity.StRiverR;
import com.water.warning.modules.monitor.service.MonitorService;
import com.water.warning.modules.notify.service.NotifyService;
import com.water.warning.modules.warning.service.WarningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 水位监测定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WaterLevelCheckJob {

    private final MonitorService monitorService;
    private final WarningService warningService;
    private final NotifyService notifyService;

    /**
     * 定时检查所有测站的水位
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void checkAllStationsWaterLevel() {
        log.info("========== 开始定时水位检查 ==========");

        try {
            // 获取所有测站的最新水位数据
            List<MonitorService.StationWaterLevelVO> stationWaterLevels = monitorService.getAllStationWaterLevels();

            for (MonitorService.StationWaterLevelVO vo : stationWaterLevels) {
                if (vo.getZ() == null) {
                    continue;
                }

                try {
                    // 分析水位并触发预警
                    warningService.analyzeAndCreateWarning(vo.getStcd(), vo.getZ(), vo.getTm());
                } catch (Exception e) {
                    log.error("测站{}水位分析异常: {}", vo.getStcd(), e.getMessage());
                }
            }

            log.info("========== 定时水位检查完成，共检查 {} 个测站 ==========", stationWaterLevels.size());
        } catch (Exception e) {
            log.error("========== 定时水位检查异常: {} ==========", e.getMessage(), e);
        }
    }

    /**
     * 重试失败的通知
     * 每5分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void retryFailedNotifications() {
        log.info("========== 开始重试失败的通知 ==========");
        try {
            notifyService.retryFailedNotifications();
            log.info("========== 重试失败的通知完成 ==========");
        } catch (Exception e) {
            log.error("========== 重试通知异常: {} ==========", e.getMessage(), e);
        }
    }
}
