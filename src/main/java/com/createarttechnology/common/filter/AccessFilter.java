package com.createarttechnology.common.filter;

import com.createarttechnology.jutil.AntiBotUtil;
import com.createarttechnology.jutil.RequestUtil;
import com.createarttechnology.jutil.StringUtil;
import com.createarttechnology.logger.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于打印访问日志
 * Created by lixuhui on 2018/2/27.
 */
public class AccessFilter implements Filter {

    private static final Logger accessLogger = Logger.getLogger("AccessLog");
    private static final Logger errorLogger = Logger.getLogger("CoreFilter");

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String scheme = req.getScheme();
        String userAgent = req.getHeader("User-Agent");
        String ip = RequestUtil.getIP(req);
        String uri = req.getRequestURI();
        if (StringUtil.isNotEmpty(req.getQueryString())) {
            uri += "?" + req.getQueryString();
        }
        String method = req.getMethod();
        String referer = req.getHeader("Referer");
        String host = req.getHeader("Host");
        Cookie[] cookies = req.getCookies();

        boolean isBot = AntiBotUtil.isBot(req);

        long startTime = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            errorLogger.error("error, e:", e);
            resp.setStatus(500);
        }
        long endTime = System.currentTimeMillis();

        int retCode = resp.getStatus();
        printAccessLog(isBot, method, scheme + "://" + host + uri, retCode, endTime - startTime, referer, ip, userAgent, cookies);
    }

    private static void printAccessLog(boolean isBot,
                                   String method,
                                   String uri,
                                   int retCode,
                                   long cost,
                                   String referer,
                                   String ip,
                                   String userAgent,
                                   Cookie[] cookies) {
        if (StringUtil.isEmpty(referer)) referer = "-";

        StringBuilder builder = new StringBuilder(256);
        builder.append(isBot ? 1 : 0).append('\t')
                .append(ip).append('\t')
                .append(method).append('\t')
                .append(uri).append('\t')
                .append(retCode).append('\t')
                .append(cost).append('\t')
                .append(referer).append('\t')
                .append(userAgent).append('\t');

        appendCookie(builder, cookies);

        accessLogger.info(builder);
    }

    private static void appendCookie(StringBuilder builder, Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            builder.append('-');
            return;
        }
        for (int i = 0; i < 3 &&  i < cookies.length; i++) {
            builder.append(cookies[i].getName()).append('=').append(cookies[i].getValue()).append(' ');
        }
        builder.setLength(builder.length() - 1);
        if (cookies.length >= 3) {
            builder.append("...");
        }
    }

    public void destroy() {

    }
}
