package com.qimu.usercenter.common;

/**
 * @author: QiMu
 * @Date: 2023年02月04日 19:43
 * @Version:1.0
 * @Description: 返回工具类
 */
public class ResultUtil {
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(0, data, message, "");
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "");
    }

    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), errorCode.getMessage(), description);
    }
}
