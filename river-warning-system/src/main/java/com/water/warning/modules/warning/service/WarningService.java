package com.water.warning.modules.warning.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.water.warning.common.exception.BusinessException;
import com.water.warning.common.result.ResultCode;
import com.water.warning.modules.base.entity.StStationUnit;
import com.water.warning.modules.base.mapper.StStationUnitMapper;
import com.water.warning.modules.monitor.entity.StRvfcchB;
import com.water.warning.modules.monitor.mapper.StRvfcchBMapper;
import com.water.warning.modules.notify.entity.StWarnNotify;
import com.water.warning.modules.notify.mapper.StWarnNotifyMapper;
import com.water.warning.modules.notify.service.NotifyService;
import com.water.warning.modules.warning.dto.WarningHandleDTO;
import com.water.warning.modules.warning.entity.StWarnInfo;
import com.water.warning.modules.warning.mapper.StWarnInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 预警服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarningService {

    private final StWarnInfoMapper warnInfoMapper;
    private final StWarnNotifyMapper warnNotifyMapper;
    private final StRvfcchBMapper rvfcchBMapper;
    private final StStationUnitMapper stationUnitMapper;
    private final NotifyService notifyService;

    /** 预警级别常量 */
    public static final int WARN_LEVEL_WARNING = 1;  // 警戒
    public static final int WARN_LEVEL_GUARANTEE = 2; // 保证

    /** 处理状态常量 */
    public static final int PROC_STAT_UNPROCESSED = 0;  // 未处理
    public static final int PROC_STAT_PROCESSING = 1;   // 处理中
    public static final int PROC_STAT_COMPLETED = 2;    // 已完成

    /**
     * 分析水位并创建预警
     */
    @Transactional(rollbackFor = Exception.class)
    public void analyzeAndCreateWarning(String stcd, BigDecimal currentZ, LocalDateTime tm) {
        log.info("开始分析水位 - 测站: {}, 当前水位: {}", stcd, currentZ);

        // 1. 获取防洪指标
        StRvfcchB threshold = rvfcchBMapper.selectByStcd(stcd);
        if (threshold == null || threshold.getWrz() == null || threshold.getGrz() == null) {
            log.warn("测站{}防洪指标未配置，跳过预警分析", stcd);
            return;
        }

        BigDecimal wrz = threshold.getWrz();   // 警戒水位
        BigDecimal grz = threshold.getGrz();  // 保证水位

        // 2. 判断预警级别
        Integer warnLevel = null;
        if (currentZ.compareTo(grz) >= 0) {
            // 达到或超过保证水位
            warnLevel = WARN_LEVEL_GUARANTEE;
            log.info("测站{}触发保证预警 - 当前水位: {}, 保证水位: {}", stcd, currentZ, grz);
        } else if (currentZ.compareTo(wrz) >= 0) {
            // 达到或超过警戒水位
            warnLevel = WARN_LEVEL_WARNING;
            log.info("测站{}触发警戒预警 - 当前水位: {}, 警戒水位: {}", stcd, currentZ, wrz);
        }

        // 3. 如果触发预警
        if (warnLevel != null) {
            createOrEscalateWarning(stcd, currentZ, wrz, grz, warnLevel, tm);
        } else {
            // 4. 水位恢复正常，检查是否需要解除预警
            checkAndResolveWarning(stcd);
        }
    }

    /**
     * 创建或升级预警
     */
    private void createOrEscalateWarning(String stcd, BigDecimal currentZ,
                                         BigDecimal wrz, BigDecimal grz,
                                         Integer warnLevel, LocalDateTime tm) {
        // 查询是否存在未处理的预警
        StWarnInfo existingWarning = warnInfoMapper.selectActiveWarning(stcd);

        if (existingWarning == null) {
            // 创建新预警
            createNewWarning(stcd, currentZ, wrz, grz, warnLevel, tm);
        } else if (existingWarning.getWarnLv() < warnLevel) {
            // 预警升级（警戒 -> 保证）
            escalateWarning(existingWarning, currentZ, warnLevel);
        } else {
            // 同级别预警，更新水位和时间
            updateWarning(existingWarning, currentZ, tm);
        }
    }

    /**
     * 创建新预警
     */
    private void createNewWarning(String stcd, BigDecimal currentZ,
                                  BigDecimal wrz, BigDecimal grz,
                                  Integer warnLevel, LocalDateTime tm) {
        StWarnInfo warnInfo = new StWarnInfo();
        warnInfo.setStcd(stcd);
        warnInfo.setWarnTm(tm);
        warnInfo.setCurrZ(currentZ);
        warnInfo.setWrz(wrz);
        warnInfo.setGrz(grz);
        warnInfo.setWarnLv(warnLevel);
        warnInfo.setProcStat(PROC_STAT_UNPROCESSED);
        warnInfo.setCreateTime(LocalDateTime.now());
        warnInfo.setUpdateTime(LocalDateTime.now());

        // 生成预警内容
        String levelName = warnLevel == WARN_LEVEL_WARNING ? "警戒" : "保证";
        warnInfo.setContent(String.format("测站%s水位%.3f米达到%s水位(警戒水位: %.3fm, 保证水位: %.3fm)",
                stcd, currentZ, levelName, wrz, grz));

        warnInfoMapper.insert(warnInfo);
        log.info("创建预警成功 - 预警ID: {}, 级别: {}", warnInfo.getWarnId(), warnLevel);

        // 生成通知记录并发送通知
        generateNotifyRecords(warnInfo, warnLevel);
    }

    /**
     * 升级预警
     */
    private void escalateWarning(StWarnInfo existingWarning, BigDecimal currentZ, Integer newLevel) {
        existingWarning.setWarnLv(newLevel);
        existingWarning.setCurrZ(currentZ);
        existingWarning.setWarnTm(LocalDateTime.now());
        existingWarning.setUpdateTime(LocalDateTime.now());

        // 更新预警内容
        String levelName = newLevel == WARN_LEVEL_WARNING ? "警戒" : "保证";
        existingWarning.setContent(String.format("测站%s水位%.3f米升级为%s预警(警戒水位: %.3fm, 保证水位: %.3fm)",
                existingWarning.getStcd(), currentZ, levelName,
                existingWarning.getWrz(), existingWarning.getGrz()));

        warnInfoMapper.updateById(existingWarning);
        log.info("预警升级成功 - 预警ID: {}, 新级别: {}", existingWarning.getWarnId(), newLevel);

        // 重新生成通知记录（包括上级单位）
        generateNotifyRecords(existingWarning, newLevel);
    }

    /**
     * 更新预警
     */
    private void updateWarning(StWarnInfo existingWarning, BigDecimal currentZ, LocalDateTime tm) {
        existingWarning.setCurrZ(currentZ);
        existingWarning.setWarnTm(tm);
        existingWarning.setUpdateTime(LocalDateTime.now());
        warnInfoMapper.updateById(existingWarning);
        log.debug("预警水位已更新 - 预警ID: {}, 新水位: {}", existingWarning.getWarnId(), currentZ);
    }

    /**
     * 检查并解除预警
     */
    private void checkAndResolveWarning(String stcd) {
        StWarnInfo activeWarning = warnInfoMapper.selectActiveWarning(stcd);
        if (activeWarning != null) {
            // 解除预警
            warnInfoMapper.resolveWarning(activeWarning.getWarnId());
            log.info("预警已解除 - 预警ID: {}, 测站: {}", activeWarning.getWarnId(), stcd);
        }
    }

    /**
     * 生成通知记录
     */
    private void generateNotifyRecords(StWarnInfo warnInfo, Integer warnLevel) {
        List<StStationUnit> units;

        if (warnLevel == WARN_LEVEL_WARNING) {
            // 警戒预警：只通知所属单位
            units = stationUnitMapper.selectOwnUnits(warnInfo.getStcd());
        } else {
            // 保证预警：通知所属单位和上级单位
            units = stationUnitMapper.selectAllUnits(warnInfo.getStcd());
        }

        if (units == null || units.isEmpty()) {
            log.warn("测站{}未配置管理单位，无法发送通知", warnInfo.getStcd());
            return;
        }

        for (StStationUnit unit : units) {
            StWarnNotify notify = new StWarnNotify();
            notify.setWarnId(warnInfo.getWarnId());
            notify.setUnitType(unit.getUnitType());
            notify.setRecvUnit(unit.getOwnUnit());
            notify.setRecvUnitName(unit.getUnitName());
            notify.setReceiver(unit.getContact());
            notify.setReceiverPhone(unit.getPhone());

            // 默认系统通知
            notify.setNotifyWay(3);
            notify.setNotifyStat(0);
            notify.setContent(warnInfo.getContent());
            notify.setCreateTime(LocalDateTime.now());
            notify.setUpdateTime(LocalDateTime.now());

            warnNotifyMapper.insert(notify);

            // 异步发送通知
            notifyService.sendNotificationAsync(notify);
        }

        log.info("生成通知记录完成 - 预警ID: {}, 通知数量: {}", warnInfo.getWarnId(), units.size());
    }

    /**
     * 处理预警
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleWarning(WarningHandleDTO dto) {
        StWarnInfo warnInfo = warnInfoMapper.selectById(dto.getWarnId());
        if (warnInfo == null) {
            throw new BusinessException(ResultCode.WARNING_NOT_FOUND);
        }

        if ("CLOSE".equalsIgnoreCase(dto.getAction())) {
            // 关闭预警（处理中）
            warnInfoMapper.updateProcStat(dto.getWarnId(), PROC_STAT_PROCESSING,
                    dto.getHandler(), dto.getHandleRemark());
            log.info("预警已处理 - 预警ID: {}, 处理人: {}", dto.getWarnId(), dto.getHandler());
        } else if ("RESOLVE".equalsIgnoreCase(dto.getAction())) {
            // 解除预警
            warnInfoMapper.updateProcStat(dto.getWarnId(), PROC_STAT_COMPLETED,
                    dto.getHandler(), dto.getHandleRemark());
            log.info("预警已解除 - 预警ID: {}, 处理人: {}", dto.getWarnId(), dto.getHandler());
        } else {
            throw new BusinessException(ResultCode.PARAM_ERROR, "无效的处理动作");
        }
    }

    /**
     * 获取所有活动预警
     */
    public List<StWarnInfo> getActiveWarnings() {
        return warnInfoMapper.selectAllActiveWarnings();
    }

    /**
     * 获取预警详情
     */
    public StWarnInfo getWarningDetail(Long warnId) {
        return warnInfoMapper.selectById(warnId);
    }

    /**
     * 获取所有预警列表
     */
    public List<StWarnInfo> getWarningList(Integer warnLv, Integer procStat) {
        LambdaQueryWrapper<StWarnInfo> wrapper = new LambdaQueryWrapper<>();
        if (warnLv != null) {
            wrapper.eq(StWarnInfo::getWarnLv, warnLv);
        }
        if (procStat != null) {
            wrapper.eq(StWarnInfo::getProcStat, procStat);
        }
        wrapper.orderByDesc(StWarnInfo::getWarnTm);
        return warnInfoMapper.selectList(wrapper);
    }
}
