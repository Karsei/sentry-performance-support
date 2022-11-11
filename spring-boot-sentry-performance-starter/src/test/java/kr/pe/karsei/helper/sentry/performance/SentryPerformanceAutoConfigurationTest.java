package kr.pe.karsei.helper.sentry.performance;

import io.sentry.HubAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(SentryPerformanceProperties.class)
@ContextConfiguration(classes = {HubAdapter.class, SentryPerformanceAutoConfiguration.class})
@TestPropertySource("classpath:application.properties")
class SentryPerformanceAutoConfigurationTest {
    // https://www.baeldung.com/spring-boot-testing-configurationproperties
    @Autowired
    private SentryPerformanceProperties sentryPerformanceProperties;

    @Test
    void test() {
        // org.springframework.boot.context.properties.bind.Binder - 447
        assertThat(sentryPerformanceProperties.isPerformanceEnabled()).isTrue();
    }

//    @Test
//    void test() {
//        contextRunner.run(context -> {
//            SentryPerformanceProperties bean = (SentryPerformanceProperties) context.getBean("sentry-kr.pe.karsei.helper.sentry.performance.SentryPerformanceProperties");
//            bean.isPerformanceEnabled();
//        });
//
//    }
//    private AnnotationConfigApplicationContext context;
//
//    @BeforeEach
//    public void setUp() {
//        this.context = new AnnotationConfigApplicationContext();
//        ConfigurableEnvironment environment = new StandardEnvironment();
//        this.context.setEnvironment(environment);
//    }
//
//    @AfterEach
//    public void close() {
//        if (this.context != null) {
//            this.context.close();
//        }
//    }
//
//    @Test
//    public void registersSentryPerformanceAutomatically() {
//        this.context.register(HubAdapter.getInstance().getClass());
//        this.context.register(SentryPerformanceAutoConfiguration.class);
//        this.context.refresh();
//        SentryAspect sentryAspect = this.context.getBean(SentryAspect.class);
//        assertThat(sentryAspect).isNotNull();
//    }
}