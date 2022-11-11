package kr.pe.karsei.helper.sentry.performance;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sentry")
@Getter @Setter
public class SentryPerformanceProperties {
    private boolean performanceEnabled;
}
