package com.malyszaryczlowiek.shop.basicMysqlSecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.Priority;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

//@Configuration
public class CookieValidityFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CookieValidityFilter.class);

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        //HttpServletResponse servletResponse = (HttpServletResponse) response;

        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null && (servletRequest.isUserInRole("ADMIN") || servletRequest.isUserInRole("CLIENT"))) {
            Cookie sessionCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("JSESSIONID"))
                    .collect(Collectors.toList()).get(0);
                sessionCookie.setMaxAge(5);
                logger.trace("session id cookie is changed to time cookie in seconds: " + sessionCookie.getMaxAge());
        }
        chain.doFilter(request, response);
    }
}
