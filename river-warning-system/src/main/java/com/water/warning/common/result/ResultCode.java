package com.water.warning.common.result;

import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
public enum ResultCode {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误 4xx
    FAIL(400, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    // 服务端错误 5xx
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    DATABASE_ERROR(500, "数据库操作失败"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // 业务错误 1xxx
    STATION_NOT_FOUND(1001, "测站不存在"),
    STATION_CODE_INVALID(1002, "测站编码无效"),
    WATER_LEVEL_INVALID(1003, "水位数值不合理"),
    WARNING_NOT_FOUND(1004, "预警记录不存在"),
    THRESHOLD_NOT_CONFIGURED(1005, "防洪指标未配置"),
    NOTIFY_FAILED(1006, "通知发送失败"),
    DUPLICATE_WARNING(1007, "重复预警");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
