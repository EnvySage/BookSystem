package com.xs.booksystem.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xs.booksystem.Context.BaseContext;
import com.xs.booksystem.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("jwt拦截器拦截请求URI:{}",requestURI);
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token) || !token.startsWith("Bearer ")){
            response.setStatus(401);
            log.info("请求未携带token，拒绝访问");
            return false;
        }
        token = token.replace("Bearer ", "");
        if (!jwtUtils.validateToken(token)){
            response.setStatus(401);
            log.info("token无效，拒绝访问");
            return false;
        }
        BaseContext.setCurrentId(jwtUtils.getUserIdFromToken(token));
        if(BaseContext.getCurrentId() == 1){
            BaseContext.setCurrentRole("ADMIN");
        }else {
            BaseContext.setCurrentRole("USER");
        }
        log.info("当前用户id:{}",BaseContext.getCurrentId());
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.removeCurrentId();
    }
}
