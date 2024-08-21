package com.example.demo.common;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class FailureHandler implements AuthenticationFailureHandler{

    @Autowired
    private HttpSession session;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // エラーメッセージをリクエストの属性に設定
        session.setAttribute("loginError", "メールアドレスまたはパスワードが正しくありません。");
        // ログイン画面にリダイレクト
        response.sendRedirect(request.getContextPath() + "/toLogin");
    }
}
