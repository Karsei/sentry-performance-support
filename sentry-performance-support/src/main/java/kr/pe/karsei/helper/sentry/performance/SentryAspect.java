package kr.pe.karsei.helper.sentry.performance;

import io.sentry.IHub;
import io.sentry.ISpan;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.util.StopWatch;

@Aspect
@RequiredArgsConstructor
public class SentryAspect {
    private final IHub iHub;

    @Pointcut("@annotation(kr.pe.karsei.helper.sentry.performance.SentryPerformance)")
    public void annotationSentryPerformance(){}

    @Around("annotationSentryPerformance()")
    public Object aroundAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ExecutionInformation methodInfo = ExecutionInformation.getInfo((MethodInvocationProceedingJoinPoint) proceedingJoinPoint);

        TransactionBreadcrumb breadcrumb = new TransactionBreadcrumb();
        TransactionSpan span = new TransactionSpan();

        StopWatch stopWatch = breadcrumb.doBefore(iHub, methodInfo);
        ISpan currentSpan = span.doBefore(iHub, methodInfo);

        Object returnVal;
        try {
            returnVal = proceedingJoinPoint.proceed();
            breadcrumb.doAfter(iHub, methodInfo, stopWatch);
            span.doAfter(iHub, methodInfo, currentSpan);

            return returnVal;
        }
        catch (Throwable e) {
            breadcrumb.doOnThrow(iHub, methodInfo, stopWatch, e);
            span.doOnThrow(iHub, methodInfo, currentSpan, e);
            throw e;
        }
    }
}
