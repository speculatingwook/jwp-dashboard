package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.adapter.Handler;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AnnotationHandlerMapping 클래스는 Handler Mapping 역할을 수행
 * 이 클래스는 지정된 패키지 내의 @Controller 어노테이션이 붙은 클래스들을 스캔하고,
 * @RequestMapping 어노테이션을 분석하여 URL과 HTTP 메서드에 따른 핸들러 실행을 매핑
 */
public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    /**
     * HandlerKey: url, RequestMethod
     * HandlerExecution: return ModelAndView
     */
    private final Map<HandlerKey, Handler> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    /**
     * basePackage에 존재하는 Controller를 어노테이션을 기준으로 handlerExecution 등록
     */
    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Map.Entry<Class<?>, Object> controller : controllers.entrySet()) {
            final Set<Method> handlerMethods = getHandlerMethods(controller.getKey());
            final Map<Method, Set<HandlerKey>> handlerKeysPerMethod = getHandlerKeysPerMethod(handlerMethods);
            mapHandlerExecutions(controller.getValue(), handlerKeysPerMethod);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }


    private Set<Method> getHandlerMethods(final Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(
                controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );
    }

    private Map<Method, Set<HandlerKey>> getHandlerKeysPerMethod(final Set<Method> methods) {
        return methods.stream()
                .collect(Collectors.toMap(
                        method -> method,
                        HandlerKey::allFrom
                ));
    }

    private void mapHandlerExecutions(final Object controller,
                                      final Map<Method, Set<HandlerKey>> handlerKeysPerMethod) {
        for (Map.Entry<Method, Set<HandlerKey>> handlerKeys : handlerKeysPerMethod.entrySet()) {
            final Method method = handlerKeys.getKey();
            final Set<HandlerKey> keys = handlerKeys.getValue();
            keys.forEach(
                    handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(controller, method))
            );
        }
    }

    /**
     *
     * @param request 기존의 HandlerKey에서 일치하는 값의 ExecutionHandler 반환
     * @return HandlerExecution
     */
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey targetHandlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(targetHandlerKey);
    }
}
