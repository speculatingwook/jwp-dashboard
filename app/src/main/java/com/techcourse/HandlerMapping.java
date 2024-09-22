package com.techcourse;

public interface HandlerMapping {
    void initialize();
    Object getHandler(String requestURI);
}
