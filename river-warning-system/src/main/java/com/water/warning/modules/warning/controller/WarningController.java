package com.water.warning.modules.warning.controller;

import com.water.warning.common.result.Result;
import com.water.warning.modules.notify.entity.StWarnNotify;
import com.water.warning.modules.notify.service.NotifyService;
import com.water.warning.modules.warning.dto.WarningHandleDTO;
import com.water.warning.modules.warning.entity.StWarnInfo;
import com.water.warning.modules.warning.service.WarningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预警管理控制器
 */
@Tag(name = "预警管理", description = "预警信息查询与处理接口")
@RestController
@RequestMapping("/warning")
@RequiredArgsConstructor
public class WarningController {

    private final WarningService warningService;
    private final NotifyService notifyService;

    /**
     * 获取所有活动预警
     */
    @Operation(summary = "获取活动预警", description = "查询所有未处理的预警信息")
    @GetMapping("/active")
    public Result<List<StWarnInfo>> getActiveWarnings() {
        List<StWarnInfo> warnings = warningService.getActiveWarnings();
        return Result.success(warnings);
    }

    /**
     * 获取预警列表
     */
    @Operation(summary = "获取预警列表", description = "根据条件查询预警列表")
    @GetMapping("/list")
    public Result<List<StWarnInfo>> getWarningList(
            @Parameter(description = "预警级别: 1-警戒 2-保证") @RequestParam(required = false) Integer warnLv,
            @Parameter(description = "处理状态: 0-未处理 1-处理中 2-已完成") @RequestParam(required = false) Integer procStat) {
        List<StWarnInfo> warnings = warningService.getWarningList(warnLv, procStat);
        return Result.success(warnings);
    }

    /**
     * 获取预警详情
     */
    @Operation(summary = "获取预警详情", description = "根据预警ID查询预警详细信息")
    @GetMapping("/{warnId}")
    public Result<StWarnInfo> getWarningDetail(@PathVariable Long warnId) {
        StWarnInfo warning = warningService.getWarningDetail(warnId);
        return Result.success(warning);
    }

    /**
     * 处理预警
     */
    @Operation(summary = "处理预警", description = "更新预警的处理状态")
    @PostMapping("/handle")
    public Result<String> handleWarning(@Valid @RequestBody WarningHandleDTO dto) {
        warningService.handleWarning(dto);
        return Result.success("预警处理成功", null);
    }

    /**
     * 获取预警的通知记录
     */
    @Operation(summary = "获取预警通知记录", description = "查询预警的通知发送记录")
    @GetMapping("/{warnId}/notifies")
    public Result<List<StWarnNotify>> getWarningNotifies(@PathVariable Long warnId) {
        List<StWarnNotify> notifies = notifyService.getNotifiesByWarnId(warnId);
        return Result.success(notifies);
    }
}
