package com.dormManager.backend.controller.exception;

import com.dormManager.backend.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidationController {

    /**
     * 处理 ValidationException 异常，通常用于捕获请求参数验证失败的情况。
     *
     * @param exception ValidationException 异常实例
     * @return 包含错误信息的 RestBean 对象，HTTP 状态码为 400
     */
    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> validateException(ValidationException exception) {
        log.warn("Resolve [{} {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(400, "请求参数有误");
    }
}
