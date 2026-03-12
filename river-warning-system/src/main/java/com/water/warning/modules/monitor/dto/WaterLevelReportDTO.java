package com.water.warning.modules.monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 水位数据上报DTO
 */
@Data
@Schema(description = "水位数据上报请求")
public class WaterLevelReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "测站编码", required = true, example = "10010001")
    @NotBlank(message = "测站编码不能为空")
    private String stcd;

    @Schema(description = "时间", required = true, example = "2024-03-12 10:00:00")
    @NotNull(message = "时间不能为空")
    private LocalDateTime tm;

    @Schema(description = "水位(m)", required = true, example = "25.500")
    @NotNull(message = "水位不能为空")
    private BigDecimal z;

    @Schema(description = "流量(m³/s)", example = "1200.000")
    private BigDecimal q;

    @Schema(description = "断面过水面积(m²)", example = "500.000")
    private BigDecimal xsa;

    @Schema(description = "断面平均流速(m/s)", example = "2.400")
    private BigDecimal xsavv;

    @Schema(description = "断面最大流速(m/s)", example = "3.200")
    private BigDecimal xsmxv;

    @Schema(description = "水势: 4-涨 5-落 6-平", example = "4")
    private String wptn;
}
