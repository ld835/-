package com.water.warning.modules.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.water.warning.common.result.Result;
import com.water.warning.modules.base.entity.StStationUnit;
import com.water.warning.modules.base.entity.StStbprpB;
import com.water.warning.modules.base.mapper.StStationUnitMapper;
import com.water.warning.modules.base.mapper.StStbprpBMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 测站管理控制器
 */
@Tag(name = "测站管理", description = "测站与单位管理接口")
@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationController {

    private final StStbprpBMapper stbprpBMapper;
    private final StStationUnitMapper stationUnitMapper;

    /**
     * 获取所有测站
     */
    @Operation(summary = "获取所有测站", description = "查询所有测站基本信息")
    @GetMapping("/list")
    public Result<List<StStbprpB>> getAllStations() {
        List<StStbprpB> stations = stbprpBMapper.selectList(null);
        return Result.success(stations);
    }

    /**
     * 获取测站详情
     */
    @Operation(summary = "获取测站详情", description = "根据测站编码查询测站详细信息")
    @GetMapping("/{stcd}")
    public Result<StStbprpB> getStationDetail(@PathVariable String stcd) {
        StStbprpB station = stbprpBMapper.selectById(stcd);
        return Result.success(station);
    }

    /**
     * 添加测站
     */
    @Operation(summary = "添加测站", description = "新增测站基本信息")
    @PostMapping
    public Result<String> addStation(@RequestBody StStbprpB station) {
        station.setCreateTime(LocalDateTime.now());
        station.setUpdateTime(LocalDateTime.now());
        stbprpBMapper.insert(station);
        return Result.success("测站添加成功", null);
    }

    /**
     * 更新测站
     */
    @Operation(summary = "更新测站", description = "更新测站基本信息")
    @PutMapping
    public Result<String> updateStation(@RequestBody StStbprpB station) {
        station.setUpdateTime(LocalDateTime.now());
        stbprpBMapper.updateById(station);
        return Result.success("测站更新成功", null);
    }

    /**
     * 获取测站的管理单位
     */
    @Operation(summary = "获取测站管理单位", description = "查询测站的所属单位和上级单位")
    @GetMapping("/{stcd}/units")
    public Result<List<StStationUnit>> getStationUnits(@PathVariable String stcd) {
        List<StStationUnit> units = stationUnitMapper.selectAllUnits(stcd);
        return Result.success(units);
    }

    /**
     * 添加测站管理单位
     */
    @Operation(summary = "添加测站管理单位", description = "为测站添加管理单位")
    @PostMapping("/units")
    public Result<String> addStationUnit(@RequestBody StStationUnit unit) {
        unit.setCreateTime(LocalDateTime.now());
        unit.setUpdateTime(LocalDateTime.now());
        stationUnitMapper.insert(unit);
        return Result.success("管理单位添加成功", null);
    }

    /**
     * 删除测站管理单位
     */
    @Operation(summary = "删除测站管理单位", description = "删除测站的管理单位关联")
    @DeleteMapping("/units/{id}")
    public Result<String> deleteStationUnit(@PathVariable Long id) {
        stationUnitMapper.deleteById(id);
        return Result.success("管理单位删除成功", null);
    }
}
