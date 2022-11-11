package kr.pe.karsei.helper.sentry.performance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 해당 Annotation 이 있을 경우, Sentry Performance 수집 대상에 포함됩니다.
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SentryPerformance {
}
