package kr.pe.karsei.helper.sentry.performance;

import io.sentry.IHub;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@RequiredArgsConstructor
public class SentryAnnotationAspect {
    private final IHub iHub;

    @Pointcut("@annotation(kr.pe.karsei.helper.sentry.performance.SentryPerformance)")
    public void annotationSentryPerformance(){}

    @Around("annotationSentryPerformance()")
    public Object aroundAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return SentryAround.trace(iHub, proceedingJoinPoint);
    }
}
