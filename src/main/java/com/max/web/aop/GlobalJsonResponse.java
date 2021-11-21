package com.max.web.aop;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import java.util.UUID;
@SuppressWarnings("rawtypes")
@ControllerAdvice
public class GlobalJsonResponse implements ResponseBodyAdvice {
    @Override
    public Object beforeBodyWrite(Object object, MethodParameter param, MediaType mediaType, Class clazz,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        String path = request.getURI().getPath();
        if (path.toLowerCase().startsWith("/swagger-resources")) {
            WebLogUtil.print(request, null);
            return object;
        }
        if (path.toLowerCase().startsWith("/v2/api-docs")) {
            WebLogUtil.print(request, null);
            return object;
        }
        if (mediaType.equals(MediaType.APPLICATION_JSON_UTF8)
                || (clazz != null && clazz.isInstance(new MappingJackson2HttpMessageConverter()))) {
            if (object instanceof BaseResultDTO) {
                WebLogUtil.print(request, null);
                return object;
            } else {
                BaseResultDTO baseResultDTO = new BaseResultDTO();
                baseResultDTO.setData(object);
                baseResultDTO.setCode(ErrorCodeEnum.SUCCESS.getCode());
                baseResultDTO.setMsg(ErrorCodeEnum.SUCCESS.getName());
                baseResultDTO.setTraceId(UUID.randomUUID().toString());
                WebLogUtil.print(request, null);
                return baseResultDTO;
            }
        }
        return object;
    }
    @Override
    public boolean supports(MethodParameter param, Class clazz) {
        return true;
    }
}
