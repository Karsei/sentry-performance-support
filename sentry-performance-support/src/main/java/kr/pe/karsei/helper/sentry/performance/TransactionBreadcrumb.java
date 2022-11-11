package kr.pe.karsei.helper.sentry.performance;

import io.sentry.Breadcrumb;
import io.sentry.IHub;
import io.sentry.SentryLevel;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.stream.Collectors;

public class TransactionBreadcrumb implements TransactionAroundInterface {
    @Override
    public StopWatch doBefore(IHub iHub, ExecutionInformation executionInformation) {
        Breadcrumb breadcrumb = createBreadcrumb(executionInformation, "[BEFORE]", SentryLevel.DEBUG);

        iHub.addBreadcrumb(breadcrumb);

        StopWatch sw = new StopWatch();
        sw.start();
        return sw;
    }

    @Override
    public void doAfter(IHub iHub, ExecutionInformation executionInformation, Object value) {
        if (value == null)  return;
        StopWatch stopWatch = (StopWatch) value;
        stopWatch.stop();

        Long duration = stopWatch.getTotalTimeMillis();
        Breadcrumb breadcrumb = createBreadcrumb(executionInformation, "[END]", SentryLevel.DEBUG);
        breadcrumb.setData("duration", String.format("%s ms", duration));

        iHub.addBreadcrumb(breadcrumb);
    }

    @Override
    public void doOnThrow(IHub iHub, ExecutionInformation executionInformation, Object value, Throwable e) {
        if (value == null)  return;
        StopWatch stopWatch = (StopWatch) value;
        stopWatch.stop();

        Long duration = stopWatch.getTotalTimeMillis();
        Breadcrumb breadcrumb = createBreadcrumb(executionInformation, "[FAIL]", SentryLevel.WARNING);
        breadcrumb.setData("duration", String.format("%s ms", duration));
        breadcrumb.setData("exception", e.getMessage() == null ? "(null)" : e.getMessage());

        iHub.addBreadcrumb(breadcrumb);
    }

    private Breadcrumb createBreadcrumb(ExecutionInformation executionInformation, String prefix, SentryLevel sentryLevel) {
        String msg = String.format("%s %s(%s)",
                prefix,
                executionInformation.getMethodName(),
                executionInformation.getParamInfoList().stream().map(o -> String.format("%s %s", o.type, o.argName)).collect(Collectors.joining(", ")));

        HashMap<String, String> paramMap = new HashMap<>();
        executionInformation.getParamInfoList().forEach(pi -> paramMap.put(pi.argName, pi.val));

        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setCategory(executionInformation.getClassName());
        breadcrumb.setLevel(sentryLevel);
        breadcrumb.setType("debug");
        breadcrumb.setMessage(msg);
        breadcrumb.setData("parameter", paramMap);

        return breadcrumb;
    }
}
