# Sentry Performance for Spring

Spring 용 Sentry Performance 기능을 위한 Breadcrumb, span 모니터링 지원 라이브러리

![introduce](./images/introduce.png)

Spring Boot 에서 Sentry Trace Sampling 을 하면 기본적으로 span 이 `http.server` 하나 밖에 나오지 않습니다. Breadcrumb 과 Span 을 직접 만들어서 메서드 및 파라미터 정보를 남기도록 하고, Spring AOP 를 이용하여 여러 메서드로부터 로깅이 되도록 도와줍니다.

## Modules

* sentry-performance-core - Sentry Core
* sentry-performance-support - Sentry Breadcrumb 및 Span 지원
* spring-boot-sentry-performance-starter - Spring Boot 를 위한 Auto-Configuration (annotation 기반으로 이용할 경우 사용)

## Usage

### 공통

`build.gradle` 에 의존성 추가

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    ...
    implementation group: 'io.sentry', name: 'sentry', version: '5.5.0'
    implementation group: 'com.github.Karsei', name: 'sentry-performance-support', version: '1.0.1'

    // annotation 기반으로 이용하고 싶을 경우 추가
    implementation group: 'com.github.Karsei', name: 'spring-boot-sentry-performance-starter', version: '1.0.1'
}
```

`application.yml`/`application.properties` 에 아래와 같이 sentry trace sampling 작성 (수치는 어플리케이션별로 적절히 입력)

```yaml
sentry:
  traces-sample-rate: 0.2
  sample-rate: 1
```

> https://docs.sentry.io/platforms/java/guides/spring-boot/configuration/sampling/

### Pointcut 적용

적용하길 원하는 Pointcut 작성

```java
public class SentryPointcut {
    @Pointcut("execution(public * com..*Controller.*(..))")
    public void controllerPointcut() {}
    @Pointcut("execution(public * com..*Service.*(..))")
    public void servicePointcut() {}

    @Pointcut("controllerPointcut() || servicePointcut()")
    public void classPointcuts() {}
}
```

Around 작성

```java
@Aspect
@RequiredArgsConstructor
public class SentryAspect {
    private final IHub iHub;

    @Around("SentryPointcut.classPointcuts()")
    public Object aroundAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return SentryAround.trace(iHub, proceedingJoinPoint);
    }
}
```

Bean 등록

```java
@Configuration
public class SentryConfiguration {
    @Bean
    public SentryAspect sentryAspect(IHub iHub) {
        return new SentryAspect(iHub);
    }
}
```

### Annotation 적용

`@SentryPerformance` 사용

```java
@RestController
@RequestMapping
public class TestController {
    @GetMapping
    @SentryPerformance
    public ResponseEntity<?> test() {
        // ...
    }
}
```
