package com.water.warning.modules.warning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 预警处理DTO
 */
@Data
@Schema(description = "预警处理请求")
public class WarningHandleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "预警ID", required = true, example = "123456789")
    @NotNull(message = "预警ID不能为空")
    private Long warnId;

    @Schema(description = "处理人", required = true, example = "张三")
    @NotBlank(message = "处理人不能为空")
    private String handler;

    @Schema(description = "处理备注", example = "已开启泄洪闸门")
    private String handleRemark;

    @Schema(description = "处理动作: CLOSE-关闭预警 RESOLVE-解除预警", example = "CLOSE")
    @NotBlank(message = "处理动作不能为空")
    private String action;
}
