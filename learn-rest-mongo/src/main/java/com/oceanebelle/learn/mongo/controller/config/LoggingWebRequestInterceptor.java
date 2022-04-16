package com.oceanebelle.learn.mongo.controller.config;


import com.oceanebelle.learn.logging.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Logs all web requests
 */
@Log4j2
public class LoggingWebRequestInterceptor implements WebRequestInterceptor {

    public static final String WEB = "web";
    public static final String PATH = "path";
    public static final String HTTP_METHOD = "httpMethod";
    public static final String SC = "sc";


    @Override
    public void preHandle(WebRequest request) throws Exception {
        log.info(LogMessageFactory.startAccess(WEB)
                .kv(HTTP_METHOD, getHttpRequest(request).getMethod())
                .kv(PATH, getHttpRequest(request).getRequestURI()));
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {

    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        var httpRequest = getHttpRequest(request);
        if (ex == null) {
            log.info(LogMessageFactory.endAccess(WEB)
                    .kv(SC, getHttpResponse(request).getStatus())
                    .kv(HTTP_METHOD, httpRequest.getMethod())
                    .kv(PATH, httpRequest.getRequestURI()));
        } else {
            log.error(LogMessageFactory.failAccess(WEB)
                    .kv(SC, getHttpResponse(request).getStatus())
                    .kv(HTTP_METHOD, httpRequest.getMethod())
                    .kv(PATH, httpRequest.getRequestURI())
                    .message(ex.getMessage()), ex);
        }
    }

    private HttpServletRequest getHttpRequest(WebRequest request) {
        return ((ServletWebRequest) request).getRequest();
    }

    private HttpServletResponse getHttpResponse(WebRequest request) {
        return ((ServletWebRequest) request).getResponse();
    }
}
