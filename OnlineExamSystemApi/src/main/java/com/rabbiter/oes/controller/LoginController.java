package com.rabbiter.oes.controller;

import com.rabbiter.oes.entity.*;
import com.rabbiter.oes.serviceimpl.LoginServiceImpl;
import com.rabbiter.oes.util.ApiResultHandler;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;

    @PostMapping("/login")
    public ApiResult login(@RequestBody Login login, HttpServletRequest request, HttpServletResponse response) {

        String username = login.getUsername().toString();
        String password = login.getPassword();
        
        // 尝试管理员登录
        Admin adminRes = null;
        try {
            // 尝试将用户名转为整数
            int adminId = Integer.parseInt(username);
            adminRes = loginService.adminLogin(adminId, password);
        } catch (NumberFormatException e) {
            // 转换失败，不是管理员账号，继续其他登录尝试
        }
        
        if (adminRes != null) {
            Cookie token = new Cookie("rb_token", adminRes.getCardId());
            token.setPath("/");
            Cookie role = new Cookie("rb_role", "0");
            role.setPath("/");

            //将cookie对象加入response响应
            response.addCookie(token);
            response.addCookie(role);

            return ApiResultHandler.buildApiResult(200, "请求成功", adminRes);
        }

        // 尝试教师登录
        Teacher teacherRes = null;
        try {
            // 尝试将用户名转为整数
            int teacherId = Integer.parseInt(username);
            teacherRes = loginService.teacherLogin(teacherId, password);
        } catch (NumberFormatException e) {
            // 转换失败，不是教师账号，继续学生登录尝试
        }
        
        if (teacherRes != null) {
            Cookie token = new Cookie("rb_token", teacherRes.getCardId());
            token.setPath("/");
            Cookie role = new Cookie("rb_role", "1");
            role.setPath("/");
            response.addCookie(token);
            response.addCookie(role);
            return ApiResultHandler.buildApiResult(200, "请求成功", teacherRes);
        }

        // 尝试学生登录
        Student studentRes = loginService.studentLogin(username, password);
        if (studentRes != null) {
            Cookie token = new Cookie("rb_token", studentRes.getCardId());
            token.setPath("/");
            Cookie role = new Cookie("rb_role", "2");
            role.setPath("/");
            response.addCookie(token);
            response.addCookie(role);
            return ApiResultHandler.buildApiResult(200, "请求成功", studentRes);
        }

        return ApiResultHandler.buildApiResult(400, "请求失败", null);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie token = new Cookie("rb_token", null);
        token.setPath("/");
        token.setMaxAge(0);
        Cookie role = new Cookie("rb_role", null);
        role.setPath("/");
        role.setMaxAge(0);
        response.addCookie(token);
        response.addCookie(role);
    }
}
