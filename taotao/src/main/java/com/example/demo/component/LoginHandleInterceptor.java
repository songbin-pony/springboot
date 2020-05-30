package com.example.demo.component;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("loginUser");
        if (user==null){
            //未登陆
            request.setAttribute("msg","没有权限，请先登陆");
            request.getRequestDispatcher("/index").forward(request,response);
            return false;
        }else {
            //已经登陆，放行
            return true;
        }
    }

    }
