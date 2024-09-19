package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Service;
import reflection.annotation.Controller;
import reflection.annotation.Repository;

import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        log.info("Classes with @Controller annotation:");
        controllerClasses.forEach(clazz -> log.info(clazz.getName()));

        log.info("Classes with @Service annotation:");
        serviceClasses.forEach(clazz -> log.info(clazz.getName()));

        log.info("Classes with @Repository annotation:");
        repositoryClasses.forEach(clazz -> log.info(clazz.getName()));
    }
}
