package com.hogwartsmini.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice // 全局捕获异常，异常集中处理，更好的使业务逻辑与异常处理剥离开，定义在类上
@ResponseBody //响应注解
@Slf4j  //日志注解
public class GlobalExceptionHandler {

    //业务异常处理
   // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 修改响应状态码,无论什么响应码 都会修改成这个码
    @ExceptionHandler({ServiceException.class}) //统一处理某一类异常，声明该方法用于捕获value所指的类型的异常（注意：当该异常的子父类都被声明时，按照先子后父的顺序进行捕获）
    public String serviceExceptionHandler(ServiceException se){
        return resultFormat(se);
    }

    //非业务异常处理
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(Exception.class) //统一处理某一类异常，声明该方法用于捕获value所指的类型的异常（注意：当该异常的子父类都被声明时，按照先子后父的顺序进行捕获）
    public String ExceptionHandler(Exception e){
        return resultFormat(e);
    }

    //系统异常处理
    @ExceptionHandler({Throwable.class}) //统一处理某一类异常，声明该方法用于捕获value所指的类型的异常（注意：当该异常的子父类都被声明时，按照先子后父的顺序进行捕获）
    public String throwableHandler(Throwable t){
        log.error(t.getMessage());
        return "系统错误 系统繁忙，请稍后重试";
    }

    public String resultFormat(Throwable t){
        log.error(t.getMessage());
        String tips="系统繁忙，请稍后重试";
        if(t instanceof ServiceException){
            return "业务异常 "+t.getMessage();
        }
        if(t instanceof  Exception){
            return "非业务异常 "+tips;
        }
        return tips;
    }
}
