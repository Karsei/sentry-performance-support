package kr.pe.karsei.helper.sentry.performance;

import io.sentry.IHub;
import io.sentry.ISpan;
import org.junit.jupiter.api.Test;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionSpanTest {
    @Test
    void testCanDoSpan() {
        // given
        IHub iHub = mock(IHub.class);
        ISpan iSpan = mock(ISpan.class);

        // when
        when(iHub.getSpan()).thenReturn(iSpan);

        // then
        final Object raw = new TestBean();
        ProxyFactory pf = new ProxyFactory(raw);
        pf.setExposeProxy(true);
        pf.addAdvisor(ExposeInvocationInterceptor.ADVISOR);
        pf.addAdvice((MethodBeforeAdvice) (method, args, target) -> {
            MethodInvocationProceedingJoinPoint jp = (MethodInvocationProceedingJoinPoint) AbstractAspectJAdvice.currentJoinPoint();
            assertThat(AopUtils.isAopProxy(AbstractAspectJAdvice.currentJoinPoint().getTarget())).isFalse();

            ExecutionInformation info = ExecutionInformation.getInfo(jp);
            TransactionSpan transactionSpan = new TransactionSpan();
            ISpan currentSpan = transactionSpan.doBefore(iHub, info);
            transactionSpan.doAfter(iHub, info, currentSpan);
            transactionSpan.doOnThrow(iHub, info, currentSpan, new RuntimeException());
        });
        TestBean proxy = (TestBean) pf.getProxy();
        proxy.setName("yeah");
    }
}