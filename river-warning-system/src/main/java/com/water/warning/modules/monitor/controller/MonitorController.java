package com.water.warning.modules.monitor.controller;

import com.water.warning.common.result.Result;
import com.water.warning.modules.monitor.dto.WaterLevelReportDTO;
import com.water.warning.modules.monitor.entity.StRvfcchB;
import com.water.warning.modules.monitor.service.MonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 监测数据控制器
 */
@Tag(name = "监测数据管理", description = "水位数据采集与查询接口")
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;

    /**
     * 水位数据上报
     */
    @Operation(summary = "水位数据上报", description = "河道测站上报实时水位数据")
    @PostMapping("/water/report")
    public Result<String> reportWaterLevel(@Valid @RequestBody WaterLevelReportDTO dto) {
        monitorService.reportWaterLevel(dto);
        return Result.success("水位数据上报成功", null);
    }

    /**
     * 获取所有测站实时水位数据
     */
    @Operation(summary = "获取所有测站实时水位", description = "查询所有测站的最新水位数据")
    @GetMapping("/realtime")
    public Result<List<MonitorService.StationWaterLevelVO>> getAllRealtimeData() {
        List<MonitorService.StationWaterLevelVO> data = monitorService.getAllStationWaterLevels();
        return Result.success(data);
    }

    /**
     * 获取指定测站实时水位数据
     */
    @Operation(summary = "获取指定测站实时水位", description = "根据测站编码查询实时水位数据")
    @GetMapping("/realtime/{stcd}")
    public Result<MonitorService.StationWaterLevelVO> getRealtimeDataByStcd(@PathVariable String stcd) {
        List<MonitorService.StationWaterLevelVO> list = monitorService.getAllStationWaterLevels();
        MonitorService.StationWaterLevelVO vo = list.stream()
                .filter(item -> stcd.equals(item.getStcd()))
                .findFirst()
                .orElse(null);
        return Result.success(vo);
    }

    /**
     * 获取测站防洪指标
     */
    @Operation(summary = "获取测站防洪指标", description = "查询测站的警戒水位和保证水位")
    @GetMapping("/threshold/{stcd}")
    public Result<StRvfcchB> getFloodControlThreshold(@PathVariable String stcd) {
        StRvfcchB threshold = monitorService.getFloodControlThreshold(stcd);
        return Result.success(threshold);
    }
}
