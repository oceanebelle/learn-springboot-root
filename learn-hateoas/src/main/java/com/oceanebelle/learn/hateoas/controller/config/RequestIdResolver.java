package com.oceanebelle.learn.hateoas.controller.config;

import brave.Tracer;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestIdResolver implements HandlerMethodArgumentResolver {

    private static final String REQUEST_ID_PARAM = "X-Request-ID";

    private final Tracer tracer;

    public RequestIdResolver(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().getType().equals(RequestId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // Injects either via header: b3 <traceId>-<spanId>
        // or headers: X-B3-TraceId and X-B3-SpanId
        if (tracer.currentSpan() != null) {
            return RequestId.builder().requestId(tracer.currentSpan().context().traceIdString()).build();
        } else {
            return RequestId.builder().requestId(null).build();
        }
    }
}
