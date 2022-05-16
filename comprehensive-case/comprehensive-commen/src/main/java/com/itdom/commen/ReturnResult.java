package com.itdom.commen;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReturnResult<T> implements Serializable {
        private String code;

        private T data;

        private String msg;

    public static <T> ReturnResult<T> success() {
        return success(null);
    }

    public static <T> ReturnResult<T> success(T data) {
        ReturnResult<T> ReturnResult = new ReturnResult<>();
        ReturnResult.setCode(ResultCode.SUCCESS.getCode());
        ReturnResult.setMsg(ResultCode.SUCCESS.getMsg());
        ReturnResult.setData(data);
        return ReturnResult;
    }

    public static <T> ReturnResult<T> failed() {
        return ReturnResult(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), ResultCode.SYSTEM_EXECUTION_ERROR.getMsg(), null);
    }

    public static <T> ReturnResult<T> failed(String msg) {
        return ReturnResult(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), msg, null);
    }

    public static <T> ReturnResult<T> judge(boolean status) {
        if (status) {
            return success();
        } else {
            return failed();
        }
    }

    public static <T> ReturnResult<T> failed(IResultCode ReturnResultCode) {
        return ReturnResult(ReturnResultCode.getCode(), ReturnResultCode.getMsg(), null);
    }

    public static <T> ReturnResult<T> failed(IResultCode ReturnResultCode, String msg) {
        return ReturnResult(ReturnResultCode.getCode(), msg, null);
    }

    private static <T> ReturnResult<T> ReturnResult(ResultCode ReturnResultCode, T data) {
        return ReturnResult(ReturnResultCode.getCode(), ReturnResultCode.getMsg(), data);
    }

    private static <T> ReturnResult<T> ReturnResult(String code, String msg, T data) {
        ReturnResult<T> ReturnResult = new ReturnResult<>();
        ReturnResult.setCode(code);
        ReturnResult.setData(data);
        ReturnResult.setMsg(msg);
        return ReturnResult;
    }

    public static boolean isSuccess(ReturnResult<?> ReturnResult) {
        return ReturnResult != null && ResultCode.SUCCESS.getCode().equals(ReturnResult.getCode());
    }


}
