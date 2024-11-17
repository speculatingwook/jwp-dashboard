import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.ControllerScanner;
import com.techcourse.DispatcherServlet;
import com.techcourse.ManualHandlerMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ManualHandlerMapping manualHandlerMapping;

    @Mock
    private ControllerScanner controllerScanner;

    @Mock
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Mock
    private HandlerExecution handlerExecution;

    @Mock
    private View view;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        when(controllerScanner.isAnnotationPresent()).thenReturn(true);
        when(annotationHandlerMapping.getHandler(request)).thenReturn(handlerExecution);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    void testServiceWithAnnotationHandler() throws Exception {
        // Given
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("key", "value");

        when(handlerExecution.handle(request, response)).thenReturn(modelAndView);

        // When
        dispatcherServlet.service(request, response);

        // Then
        verify(annotationHandlerMapping).getHandler(request);
        verify(handlerExecution).handle(request, response);
        verify(view).render(eq(modelAndView.getModel()), eq(request), eq(response));
        verify(request).setAttribute("key", "value");
    }

    @Test
    void testServiceWithManualHandler() throws Exception {
        // Given
        when(controllerScanner.isAnnotationPresent()).thenReturn(false);
        when(manualHandlerMapping.getHandler(request)).thenReturn((req, res) -> "/view.jsp");

        // When
        dispatcherServlet.service(request, response);

        // Then
        verify(manualHandlerMapping).getHandler(request);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testServiceWithRedirect() throws Exception {
        // Given
        when(controllerScanner.isAnnotationPresent()).thenReturn(false);
        when(manualHandlerMapping.getHandler(request)).thenReturn((req, res) -> "redirect:/newLocation");

        // When
        dispatcherServlet.service(request, response);

        // Then
        verify(manualHandlerMapping).getHandler(request);
        verify(response).sendRedirect("/newLocation");
    }

    @Test
    void testServiceWithException() {
        // Given
        when(controllerScanner.isAnnotationPresent()).thenReturn(true);
        when(annotationHandlerMapping.getHandler(request)).thenThrow(new RuntimeException("Test Exception"));

        // When & Then
        assertThrows(ServletException.class, () -> dispatcherServlet.service(request, response));
    }

    @Test
    void testModelAndViewFunctionality() {
        // Given
        ModelAndView modelAndView = new ModelAndView(view);

        // When
        modelAndView.addObject("key1", "value1");
        modelAndView.addObject("key2", "value2");

        // Then
        assertEquals("value1", modelAndView.getObject("key1"));
        assertEquals("value2", modelAndView.getObject("key2"));

        Map<String, Object> model = modelAndView.getModel();
        assertEquals(2, model.size());
        assertEquals("value1", model.get("key1"));
        assertEquals("value2", model.get("key2"));

        assertSame(view, modelAndView.getView());

        // Verify that the model is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> model.put("key3", "value3"));
    }
}