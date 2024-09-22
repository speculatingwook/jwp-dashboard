package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handler Mapping 역할을 하는 클래스
 * 일단 현재까지는 AnnotationHandelrMappingTest에서 samples 패키지만 불러옴(TestController 존재)
 * Reflection으로 어노테이션에 맞는 함수를 불러와, HandlerExecution에 등록
 */
public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    /**
     * HandlerKey: url, RequestMethod
     * HandlerExecution: return ModelAndView
     */
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    private Set<Class<?>> getAllClassesInPackage(final Object... packageName) {
        Set<Class<?>> annotatedClasses = new HashSet<>();
        Reflections reflections = new Reflections(packageName);

        // add all annotated classes
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        log.info("annotated classes: " + annotatedClasses);
        return annotatedClasses;
    }

    /**
     * basePackage에 존재하는 Controller를 어노테이션을 기준으로 handlerExecution 등록
     */
    public void initialize() {
        Set<Class<?>> controllerAnnotatedClasses = getAllClassesInPackage(basePackage);
        for (Class<?> controller : controllerAnnotatedClasses) {
            setHandlerExecutions(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void setHandlerExecutions(Class<?> controller){
        for(Method method : controller.getDeclaredMethods()) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            try{
                Object instance = controller.getDeclaredConstructor().newInstance();

                HandlerExecution handlerExecution = new HandlerExecution(instance, method);
                HandlerKey newHandlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);

                handlerExecutions.put(newHandlerKey, handlerExecution);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *
     * @param request request를 기준으로 기존의 HandlerKey에서 일치하는 값을 찾고, 찾게 되면 ExecutionHandler 반환
     * @return
     */
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey targetHandlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(targetHandlerKey);
    }
}
