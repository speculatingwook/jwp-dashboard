package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface HandlerMapping {
    void initialize();
    Object getHandler(HttpServletRequest request);
}
