package com.max.web.aop;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public BaseResultDTO defaultErrorHandler(HttpServletRequest request, Exception exception) throws Exception {
        WebLogUtil.print(request, exception);
        BaseResultDTO result = new BaseResultDTO();
        if (exception instanceof MethodArgumentNotValidException) {
            List<String> errorList = new ArrayList<String>();
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
            if (methodArgumentNotValidException.getBindingResult() != null) {
                if (!CollectionUtils.isEmpty(methodArgumentNotValidException.getBindingResult().getAllErrors())) {
                    for (ObjectError objectError : methodArgumentNotValidException.getBindingResult().getAllErrors()) {
                        FieldError fieldError = (FieldError) objectError;
                        errorList.add(fieldError.getField() + fieldError.getDefaultMessage());
                    }
                }
            }
            result.setData(ErrorCodeEnum.PARAM_ERROR.getName());
            result.setMsg(errorList);
            result.setCode(ErrorCodeEnum.PARAM_ERROR.getCode());
            return result;
        }
        if (exception instanceof HttpMessageNotReadableException) {
            result.setData("post请求内容为空");
            result.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(exception.getLocalizedMessage());
            return result;
        }
        if (exception instanceof NoSuchElementException) {
            result.setData("该记录不存在");
            result.setCode(ErrorCodeEnum.RECORD_NOT_EXISTS.getCode());
            result.setMsg(exception.getMessage());
            return result;
        }
        if (exception instanceof MissingServletRequestParameterException) {
            result.setData(ErrorCodeEnum.PARAM_ERROR.getName());
            result.setCode(ErrorCodeEnum.PARAM_ERROR.getCode());
            result.setMsg(exception.getMessage());
            return result;
        }
        if (exception instanceof UndeclaredThrowableException) {
            Throwable throwable = ((UndeclaredThrowableException) exception).getUndeclaredThrowable();
            if (throwable instanceof ParamValidException) {
                ParamValidException paramValidException = (ParamValidException) throwable;
                if (!CollectionUtils.isEmpty(paramValidException.getErrorList())) {
                    List<String> errorList = new ArrayList<String>();
                    for (FieldError fieldError : paramValidException.getErrorList()) {
                        errorList.add(fieldError.getField() + fieldError.getDefaultMessage());
                    }
                    result.setData(ErrorCodeEnum.PARAM_ERROR.getName());
                    result.setMsg(errorList);
                    result.setCode(ErrorCodeEnum.PARAM_ERROR.getCode());
                }
                return result;
            }
            result.setMsg(throwable.getMessage());
            result.setData(ErrorCodeEnum.SYSTEM_ERROR.getName());
            result.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            return result;
        }
        if (exception instanceof Exception) {
            result.setMsg(exception.getMessage());
            result.setData(ErrorCodeEnum.SYSTEM_ERROR.getName());
            result.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            return result;
        }
        return result;
    }
}
