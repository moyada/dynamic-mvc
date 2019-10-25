package io.moyada.dynamicmvc.handler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Component
public class HandlerMapperRegister implements ApplicationContextAware {

    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public void addMapper(String urlPath, Object handler, Method method,
                          RequestMethod requestMethod, MediaType... mediaTypes) {
        if (!handler.getClass().isAnnotationPresent(RestController.class)) {
            throw new IllegalArgumentException("handle must be declare @RestController");
        }

        RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(urlPath)
                .methods(requestMethod);

        String[] params = getParams(method.getParameters());
        if (params != null) {
            builder.params(params);
        }

        for (MediaType mediaType : mediaTypes) {
            builder.produces(mediaType.getType() + "/" + mediaType.getSubtype());
        }

        RequestMappingInfo requestMappingInfo = builder.build();
        requestMappingHandlerMapping.registerMapping(requestMappingInfo, handler, method);
    }

    private String[] getParams(Parameter[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return null;
        }

        List<String> params = new ArrayList<>(parameters.length);

        for (Parameter parameter : parameters) {
            RequestParam param = AnnotationUtils.getAnnotation(parameter, RequestParam.class);
            if (param != null) {
                String name = param.name();
                if (!StringUtils.isEmpty(name)) {
                    params.add(name);
                    continue;
                }

                name = param.value();
                if (!StringUtils.isEmpty(name)) {
                    params.add(name);
                    continue;
                }
            }

            PathVariable path = AnnotationUtils.getAnnotation(parameter, PathVariable.class);
            if (path != null) {
                continue;
            }

            params.add(parameter.getName());
        }

        if (params.isEmpty()) {
            return null;
        }

        return params.toArray(new String[0]);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
    }
}
