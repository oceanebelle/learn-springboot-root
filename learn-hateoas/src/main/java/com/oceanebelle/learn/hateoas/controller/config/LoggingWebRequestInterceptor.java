package com.oceanebelle.learn.hateoas.controller.config;

import com.oceanebelle.learn.hateoas.logging.LogMessage;
import com.oceanebelle.learn.hateoas.logging.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * Logs all web requests
 */
@Log4j2
public class LoggingWebRequestInterceptor implements WebRequestInterceptor {

    public static final String WEB = "web";

    @Override
    public void preHandle(WebRequest request) throws Exception {
        log.info(LogMessageFactory.startAccess(WEB)
                .kv("httpMethod", getHttpRequest(request).getMethod())
                .kv("uri", getHttpRequest(request).getRequestURI()));
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {

    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        var httpRequest = getHttpRequest(request);
        if (ex == null) {
            log.info(LogMessageFactory.endAccess(WEB)
                    .kv("httpMethod", httpRequest.getMethod())
                    .kv("uri", httpRequest.getRequestURI()));
        } else {
            log.error(LogMessageFactory.failAccess(WEB)
                    .kv("httpMethod", httpRequest.getMethod())
                    .kv("uri", httpRequest.getRequestURI()), ex);
        }
    }

    public HttpServletRequest getHttpRequest(WebRequest request) {
        return ((ServletWebRequest) request).getRequest();
    }
}
