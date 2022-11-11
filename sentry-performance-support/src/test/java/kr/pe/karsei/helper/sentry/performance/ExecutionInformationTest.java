package kr.pe.karsei.helper.sentry.performance;

import org.junit.jupiter.api.Test;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ExecutionInformationTest {
    @Test
    void testCanGetInfo() {
        final Object raw = new TestBean();

        ProxyFactory pf = new ProxyFactory(raw);
        pf.setExposeProxy(true);
        pf.addAdvisor(ExposeInvocationInterceptor.ADVISOR);
        pf.addAdvice((MethodBeforeAdvice) (method, args, target) -> {
            MethodInvocationProceedingJoinPoint jp = (MethodInvocationProceedingJoinPoint) AbstractAspectJAdvice.currentJoinPoint();
            assertThat(AopUtils.isAopProxy(AbstractAspectJAdvice.currentJoinPoint().getTarget())).isFalse();

            ExecutionInformation info = ExecutionInformation.getInfo(jp);
            assertThat(info.getClassName()).isEqualTo("TestBean");
            assertThat(info.getMethodName()).isEqualTo("setName");
            assertThat(info.getParamInfoList().get(0).type).isEqualTo("String");
            assertThat(info.getParamInfoList().get(0).argName).isEqualTo("name");
            assertThat(info.getParamInfoList().get(0).val).isEqualTo("yeah");
        });
        TestBean proxy = (TestBean) pf.getProxy();
        proxy.setName("yeah");
    }
}