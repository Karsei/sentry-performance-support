package kr.pe.karsei.helper.sentry.performance;

import io.sentry.IHub;
import io.sentry.ISpan;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.util.StopWatch;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SentryAround {
    public static Object trace(IHub iHub, ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
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
