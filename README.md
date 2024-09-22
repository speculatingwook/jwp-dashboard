# 만들면서 배우는 스프링

## @MVC 구현하기

### 학습목표
- @MVC를 구현하면서 MVC 구조와 MVC의 각 역할을 이해한다.
- 새로운 기술을 점진적으로 적용하는 방법을 학습한다.

### 시작 가이드
1. 미션을 시작하기 전에 학습 테스트를 먼저 진행합니다.
    - [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
    - [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
    - [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
    - [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)
    - 나머지 학습 테스트는 강의 시간에 풀어봅시다.
2. 학습 테스트를 완료하면 LMS의 1단계 미션부터 진행합니다.


# 0단계 - Reflection, DI 학습 미션
+ [x] study 디렉토리 안의 test를 아래 순서대로 모두 수행한다.
+ [x] study/src/test/java/reflection 디렉토리 내부의 테스트를 수행한다.
   + [x] [Junit3TestRunner](https://github.com/gdsc-konkuk/java-mvc/blob/main/study/src/test/java/reflection/Junit3TestRunner.java)
   + [x] [Junit4TestRunner](https://github.com/gdsc-konkuk/java-mvc/blob/main/study/src/test/java/reflection/Junit4TestRunner.java)
   + [x] [ReflectionTest](https://github.com/gdsc-konkuk/java-mvc/blob/main/study/src/test/java/reflection/ReflectionTest.java)
   + [x] [ReflectionsTest](https://github.com/gdsc-konkuk/java-mvc/blob/main/study/src/test/java/reflection/ReflectionsTest.java)
+ [x] study/src/test/java/servlet 디렉토리 내부의 테스트를 수행한다.

# 1단계 - @MVC 구현하기
+ [ ] AnnotationHandlerMappingTest 테스트를 통과한다.
+ [ ] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있게 한다.

# 2단계 - 점진적인 리팩터링
+ [ ] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있게 한다.
+ [ ] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 한다.
+ [ ] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 한다.

# 3단계 - JSON View 구현하기
### 요구 사항
1. [ ] JspView 클래스를 구현한다.
2. [ ] JsonView 클래스를 구현한다.
3. [ ] Legacy MVC 제거하기
+ [ ] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환하게 한다.
+ [ ] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작하게 한다.