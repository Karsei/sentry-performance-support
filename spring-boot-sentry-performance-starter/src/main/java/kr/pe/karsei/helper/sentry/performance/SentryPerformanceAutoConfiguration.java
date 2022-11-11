package kr.pe.karsei.helper.sentry.performance;

import io.sentry.IHub;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "sentry.performance-enabled", havingValue = "true", matchIfMissing = true)
public class SentryPerformanceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SentryPerformanceProperties sentryPerformanceProperties() {
        return new SentryPerformanceProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public SentryAspect sentryAspect(IHub iHub) {
        return new SentryAspect(iHub);
    }
}
