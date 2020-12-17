package com.hogwartsmini.demo.config.Intercepors;

import com.hogwartsmini.demo.common.ServiceException;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.common.UserBaseStr;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 全局拦截，
 * 1. preHandle()在调用接口前执行，
 * 2. postHandle()在调用接口过程中执行
 * 3. afterCompletion()在调用接口后执行
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired //这个单词的意思是自动装配，就是把spring容器中这个类装配到当前类里
    private TokenDb tokenDb;

    //这个方法是在访问接口前执行的，我们只需要在这里写验证登录状态的业务逻辑，就可以在用户调用指定接口之前验证登录状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("====preHandle====");
        String requestUri=request.getRequestURI();
        log.info("===request.getRequestUri()==="+requestUri);
        //下方对接口拦截判断是否传入token，但是有些接口不需要判断，所以在下面过滤掉,放行
        boolean passFlag=false;
        if(requestUri.contains("/user/login")
                || requestUri.contains("/user/register")
                ||requestUri.contains("swagger")
                //过滤spring默认错误页面
                || requestUri.equals("/error")
                //过滤csrf
                || requestUri.equals("/csrf")
                //过滤http://127.0.0.1:8093/v2/api-docs
                || requestUri.equals("/favicon.ico")//演示map local 不用校验是否登录
                || requestUri.equals("/report/showMapLocal")
                || requestUri.equals("/")){
            passFlag=true;
        }
        if(passFlag){
            return true; // 如果是包含上面接口，放行，直接return。下方语句不执行
        }
        //1、从请求的Header获取客户端附加token
        String tokenStr=request.getHeader(UserBaseStr.LOGIN_TOKEN);

        //2、 如果请求中无token， 响应码设401，抛出业务异常:客户端未传token
        if(StringUtils.isEmpty(tokenStr)){
            response.setStatus(401);
            ServiceException.throwEx("访问失败，客户端未传token");
        }

        //3、从tokenDb中 根据token查询TokenDto
       // 如果为空，则响应码设401，抛出业务异常:用户未登录 否则，允许请求通过
        if(Objects.isNull(tokenDb.getUserInfo(tokenStr))){
            response.setStatus(401);
            ServiceException.throwEx("用户未登录");
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.info("====postHandle====");
        log.info("===request.getRequestUri()==="+request.getRequestURI());

    }


    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("====afterCompletion====");
        log.info("===request.getRequestUri()==="+request.getRequestURI());
    }
}
