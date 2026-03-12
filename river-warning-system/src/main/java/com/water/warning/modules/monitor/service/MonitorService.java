package com.water.warning.modules.monitor.service;

import com.water.warning.common.exception.BusinessException;
import com.water.warning.common.result.ResultCode;
import com.water.warning.modules.base.entity.StStbprpB;
import com.water.warning.modules.base.mapper.StStbprpBMapper;
import com.water.warning.modules.monitor.dto.WaterLevelReportDTO;
import com.water.warning.modules.monitor.entity.StRiverR;
import com.water.warning.modules.monitor.entity.StRvfcchB;
import com.water.warning.modules.monitor.mapper.StRiverRMapper;
import com.water.warning.modules.monitor.mapper.StRvfcchBMapper;
import com.water.warning.modules.warning.entity.StWarnInfo;
import com.water.warning.modules.warning.service.WarningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 监测数据服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorService {

    private final StRiverRMapper riverRMapper;
    private final StRvfcchBMapper rvfcchBMapper;
    private final StStbprpBMapper stbprpBMapper;
    private final WarningService warningService;

    /**
     * 上报水位数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void reportWaterLevel(WaterLevelReportDTO dto) {
        log.info("接收到水位数据上报 - 测站: {}, 时间: {}, 水位: {}", dto.getStcd(), dto.getTm(), dto.getZ());

        // 1. 校验测站编码是否存在
        StStbprpB station = stbprpBMapper.selectById(dto.getStcd());
        if (station == null) {
            throw new BusinessException(ResultCode.STATION_NOT_FOUND);
        }

        // 2. 校验水位数值是否合理
        validateWaterLevel(dto.getZ());

        // 3. 保存水位数据到河道水情表
        StRiverR riverR = new StRiverR();
        riverR.setStcd(dto.getStcd());
        riverR.setTm(dto.getTm());
        riverR.setZ(dto.getZ());
        riverR.setQ(dto.getQ());
        riverR.setXsa(dto.getXsa());
        riverR.setXsavv(dto.getXsavv());
        riverR.setXsmxv(dto.getXsmxv());
        riverR.setWptn(dto.getWptn());
        riverR.setCreateTime(LocalDateTime.now());

        riverRMapper.insert(riverR);
        log.info("水位数据已保存 - 测站: {}", dto.getStcd());

        // 4. 触发预警分析
        warningService.analyzeAndCreateWarning(dto.getStcd(), dto.getZ(), dto.getTm());
    }

    /**
     * 校验水位数值是否合理
     */
    private void validateWaterLevel(java.math.BigDecimal z) {
        if (z == null) {
            throw new BusinessException(ResultCode.WATER_LEVEL_INVALID, "水位不能为空");
        }
        // 水位应该在合理范围内 (-10m ~ 100m)
        if (z.compareTo(new java.math.BigDecimal("-10")) < 0
                || z.compareTo(new java.math.BigDecimal("100")) > 0) {
            throw new BusinessException(ResultCode.WATER_LEVEL_INVALID, "水位数值超出合理范围");
        }
    }

    /**
     * 获取所有测站的实时水位数据
     */
    public List<StRiverR> getAllRealtimeData() {
        return riverRMapper.selectAllLatest();
    }

    /**
     * 获取指定测站的实时水位数据
     */
    public StRiverR getRealtimeDataByStcd(String stcd) {
        return riverRMapper.selectLatestByStcd(stcd);
    }

    /**
     * 获取测站防洪指标
     */
    public StRvfcchB getFloodControlThreshold(String stcd) {
        return rvfcchBMapper.selectByStcd(stcd);
    }

    /**
     * 获取所有测站的实时监测数据（含预警状态）
     */
    public List<StationWaterLevelVO> getAllStationWaterLevels() {
        List<StRiverR> riverRList = riverRMapper.selectAllLatest();
        return riverRList.stream().map(riverR -> {
            StationWaterLevelVO vo = new StationWaterLevelVO();
            vo.setStcd(riverR.getStcd());
            vo.setTm(riverR.getTm());
            vo.setZ(riverR.getZ());

            // 获取防洪指标
            StRvfcchB threshold = rvfcchBMapper.selectByStcd(riverR.getStcd());
            if (threshold != null) {
                vo.setWrz(threshold.getWrz());
                vo.setGrz(threshold.getGrz());

                // 判断预警状态
                if (threshold.getGrz() != null && riverR.getZ().compareTo(threshold.getGrz()) >= 0) {
                    vo.setWarnStatus(2); // 保证预警
                } else if (threshold.getWrz() != null && riverR.getZ().compareTo(threshold.getWrz()) >= 0) {
                    vo.setWarnStatus(1); // 警戒预警
                } else {
                    vo.setWarnStatus(0); // 正常
                }
            } else {
                vo.setWarnStatus(0);
            }

            // 获取测站信息
            StStbprpB station = stbprpBMapper.selectById(riverR.getStcd());
            if (station != null) {
                vo.setStnm(station.getStnm());
            }

            return vo;
        }).toList();
    }

    /**
     * 测站水位VO
     */
    @lombok.Data
    public static class StationWaterLevelVO {
        private String stcd;
        private String stnm;
        private LocalDateTime tm;
        private java.math.BigDecimal z;
        private java.math.BigDecimal wrz;
        private java.math.BigDecimal grz;
        private Integer warnStatus; // 0-正常 1-警戒 2-保证
    }
}
