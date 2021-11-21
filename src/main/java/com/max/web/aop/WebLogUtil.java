package com.max.web.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class WebLogUtil {
    private static Logger logger = LoggerFactory.getLogger(WebLogUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void print(HttpServletRequest request, Exception exception) {
        try {
            WebLog webLog = new WebLog();
            Long start = System.currentTimeMillis();
            webLog.setMethod(request.getMethod());
            webLog.setPath(request.getRequestURI());
            webLog.setUrl(request.getRequestURL().toString());
            webLog.setHeader(toHeaderString(request));
            webLog.setParams(request.getParameterMap());
            if (null != exception) {
                webLog.setError(exception.getMessage());
                webLog.setTime(System.currentTimeMillis() - start);
                logger.error(objectMapper.writeValueAsString(webLog), exception);
            } else {
                webLog.setTime(System.currentTimeMillis() - start);
                logger.info(objectMapper.writeValueAsString(webLog));
            }
        } catch (Exception e) {
            logger.error("Global log error", e);
        }
    }

    public static void print(ServerHttpRequest request, Exception exception) {
        try {
            WebLog webLog = new WebLog();
            Long start = System.currentTimeMillis();
            webLog.setMethod(request.getMethodValue());
            webLog.setPath(request.getURI().getPath());
            webLog.setUrl(request.getURI().toString());
            webLog.setHeader(toHeaderString(request));
            if (null != exception) {
                webLog.setError(exception.getMessage());
                webLog.setTime(System.currentTimeMillis() - start);
                logger.error(objectMapper.writeValueAsString(webLog), exception);
            }else {
                webLog.setTime(System.currentTimeMillis() - start);
                logger.info(objectMapper.writeValueAsString(webLog));
            }
        } catch (Exception e) {
            logger.error("Global log error", e);
        }
    }

    private static String toHeaderString(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            stringBuilder.append(key).append("=").append(value).append(" ");
        }
        return stringBuilder.toString();
    }

    private static String toHeaderString(ServerHttpRequest request) {
        HttpHeaders headerNames = request.getHeaders();
        return headerNames.toString();
    }

}
