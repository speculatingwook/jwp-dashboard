package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Handler Adapter 역할을 하는 클래스인 것 같다.
 * request를 받고, 해당 컨트롤러의 메서드를 직접적으로 실행시켜주는 역할을 하는 것 같은데, 어떻게 구현할지 감이 안온다.
 * HandlerKey가 AnnotationHandlerMapping에 있어서 그런 것 같다. 어떤 매핑 정보인지 알고, 해당 매핑에 따라 함수를 구현해주어야 하는데,,
 * 아니면 이미 알고 있다고 가정하고 바로 실행에 옮기는? 그런건가?
 */
public class HandlerExecution {
    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Object controller;
    private final Method handlerMethod;

    public HandlerExecution(Object controller, Method handlerMethod) {
        this.controller = controller;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object[] args = prepareMethodArguments(request, response);
        log.info("contoller method: " + controller.getClass().getName());
        log.info("handler method: " + handlerMethod.getName());

        try {
            Object result = handlerMethod.invoke(controller, args);
            if (result instanceof ModelAndView) {
                return (ModelAndView) result;
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            log.error("Error invoking handler method", e);
            throw new Exception("Handler method invocation failed", e);
        } catch (InvocationTargetException e) {
            log.error("Handler method threw an exception", e.getTargetException());
            throw new Exception("Handler method execution failed", e.getTargetException());
        }
        return null;
    }

    private Object[] prepareMethodArguments(HttpServletRequest request, HttpServletResponse response) {
        return new Object[]{request, response};
    }
}