package com.atguigu.common.config.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import result.Result;
import result.ResultCodeEnum;



@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行全局异常处理");
    }
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("执行特定异常处理...");
    }
    @ExceptionHandler(GuiguException.class)
    @ResponseBody
    public Result error(GuiguException e){
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMsg());
    }

    /**
     * spring security异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error( AccessDeniedException e) throws AccessDeniedException {
        return Result.fail().code(205).message("没有操作权限");
    }

}
