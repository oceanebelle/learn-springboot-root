package com.oceanebelle.learn.mongo.controller.config;

import com.oceanebelle.learn.logging.plugin.LogCaptorAppender;
import com.oceanebelle.learn.logging.test.LogHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class LoggingWebRequestInterceptorTest {

    LoggingWebRequestInterceptor interceptor;

    ServletWebRequest servletWebRequest;

    @Mock
    HttpServletRequest httpRequest;
    @Mock
    HttpServletResponse httpResponse;

    @BeforeEach
    public void setup() {

        Mockito.when(httpRequest.getMethod()).thenReturn("GET");
        Mockito.when(httpRequest.getRequestURI()).thenReturn("/path");
        Mockito.when(httpResponse.getStatus()).thenReturn(200);

        servletWebRequest = new ServletWebRequest(httpRequest, httpResponse);
        interceptor = new LoggingWebRequestInterceptor();

        LogHelper.captureLogFor(LoggingWebRequestInterceptor.class);

    }

    @Test
    void logRequestBeforeAndAfter() throws Exception {
        interceptor.preHandle(servletWebRequest);
        interceptor.postHandle(servletWebRequest, null);
        interceptor.afterCompletion(servletWebRequest, null);

        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        Assertions.assertThat(logCaptor.getLogCount()).isEqualTo(2);

        Assertions.assertThat(logCaptor.getLog(0)).matches("action=ACCESS method=web state=START httpMethod=GET path=/path");
        Assertions.assertThat(logCaptor.getLog(1)).matches("action=ACCESS method=web state=COMPLETE httpMethod=GET path=/path sc=200 tookMs=\\d+");
    }

    @Test
    void logRequestBeforeAndAfterException() throws Exception {
        interceptor.preHandle(servletWebRequest);
        interceptor.postHandle(servletWebRequest, null);
        interceptor.afterCompletion(servletWebRequest, new RuntimeException("failed request"));

        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        Assertions.assertThat(logCaptor.getLogCount()).isEqualTo(2);

        Assertions.assertThat(logCaptor.getLog(0)).matches("action=ACCESS method=web state=START httpMethod=GET path=/path");
        Assertions.assertThat(logCaptor.getLog(1)).matches("action=ACCESS method=web state=FAIL httpMethod=GET message=\"failed request\" path=/path sc=200 tookMs=\\d+.*");
    }

}
