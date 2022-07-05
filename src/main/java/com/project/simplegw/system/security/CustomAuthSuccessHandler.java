package com.project.simplegw.system.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.simplegw.member.data.MemberData;
import com.project.simplegw.member.services.MemberLoginService;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    private static final String SUCCESS_URL = "/main";
    private final MemberLoginService memberLoginService;
    private final CustomAuthFailureHandler failureHandler;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public CustomAuthSuccessHandler(MemberLoginService memberLoginService, CustomAuthFailureHandler failureHandler) {
        this.memberLoginService = memberLoginService;
        this.failureHandler = failureHandler;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String ipAddr = request.getHeader("X-FORWARDED-FOR");
        if(ipAddr == null || ipAddr.isBlank()) {
            ipAddr = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddr == null || ipAddr.isBlank()) {
            ipAddr = request.getRemoteAddr();
        }

        String browser = null;
        String userAgent = request.getHeader("User-Agent");
        String device = null;

        // browser check
        if(userAgent.contains("Trident")) { // IE
            browser = "IE";
        } else if(userAgent.contains("Edge") || userAgent.contains("Edg")) { // Edge
            browser = "Edge";
        } else if(userAgent.contains("Whale")) { // Naver Whale
            browser = "Naver Whale";
        } else if(userAgent.contains("Opera") || userAgent.contains("OPR")) { // Opera
            browser = "Opera";
        } else if(userAgent.contains("Firefox")) { // Firefox
            browser = "Firefox";
        } else if(userAgent.contains("Chrome")) { // Chrome
            browser = "Chrome";
        } else if(userAgent.contains("Safari")) { // Safari
            browser = "Safari";
        } else {
            browser = "Others";
        }

        // device check
        if(userAgent.toUpperCase().indexOf("MOBI") > -1) {
            device = "Mobile";
        } else {
            device = "PC";
        }

        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        MemberData userInfo = memberLoginService.getMemberData(loginUser);

        log.info(
            "Logged in user - ID: {}, {} {} {}, Host ip: {}, Browser: {}, Device: {}, User-Agent: {}",
            userInfo.getId(), userInfo.getTeam(), userInfo.getJobTitle(), userInfo.getName(), ipAddr, browser, device, userAgent
        );
        
        failureHandler.clearFailureCount(loginUser.getUsername());

        response.sendRedirect(SUCCESS_URL);
    }
}