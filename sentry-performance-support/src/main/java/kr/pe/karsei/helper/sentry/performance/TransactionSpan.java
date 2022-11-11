package kr.pe.karsei.helper.sentry.performance;

import io.sentry.IHub;
import io.sentry.ISpan;
import io.sentry.SpanStatus;

import java.util.ArrayList;

public class TransactionSpan implements TransactionAroundInterface {
    @Override
    public ISpan doBefore(IHub iHub, ExecutionInformation executionInformation) {
        ISpan currentSpan = iHub.getSpan();
        if (currentSpan == null)    return null;

        String spanName = String.format("%s#%s", executionInformation.getClassName(), executionInformation.getMethodName());
        StringBuilder descBuilder = new StringBuilder(String.format("%s(", spanName));

        ArrayList<String> argsStrArr = new ArrayList<>();
        for (ExecutionInformation.MethodParameterInformation paramInfo : executionInformation.getParamInfoList()) {
            argsStrArr.add(String.format("%s %s = %s", paramInfo.type, paramInfo.argName, paramInfo.val));
        }
        descBuilder.append(String.join(", ", argsStrArr));
        descBuilder.append(")");

        return currentSpan.startChild(spanName, descBuilder.toString());
    }

    @Override
    public void doAfter(IHub iHub, ExecutionInformation executionInformation, Object value) {
        if (value == null)  return;
        ISpan iSpan = (ISpan) value;
        iSpan.finish();
    }

    @Override
    public void doOnThrow(IHub iHub, ExecutionInformation executionInformation, Object value, Throwable e) {
        if (value == null)  return;
        ISpan iSpan = (ISpan) value;
        iSpan.finish(SpanStatus.INTERNAL_ERROR);
    }
}
