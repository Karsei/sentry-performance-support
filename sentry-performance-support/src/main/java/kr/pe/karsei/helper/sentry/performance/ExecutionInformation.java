package kr.pe.karsei.helper.sentry.performance;

import lombok.Getter;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ExecutionInformation {
    private final String signature;
    private final String className;
    private final String methodName;
    private final List<MethodParameterInformation> paramInfoList;

    public ExecutionInformation(String signature, String className, String methodName, List<MethodParameterInformation> paramInfoList) {
        this.signature = signature;
        this.className = className;
        this.methodName = methodName;
        this.paramInfoList = paramInfoList;
    }

    public static ExecutionInformation getInfo(MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) methodInvocationProceedingJoinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        List<String> paramNames = Arrays.stream(signature.getParameterNames()).collect(Collectors.toList());
        List<String> paramTypes = Arrays.stream(signature.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.toList());
        List<String> argValues = Arrays.stream(methodInvocationProceedingJoinPoint.getArgs()).map(o -> o != null ? o.toString() : "null").collect(Collectors.toList());

        List<MethodParameterInformation> paramInfos = new ArrayList<>();
        for (int i = 0; i < paramNames.size(); i++) {
            paramInfos.add(new MethodParameterInformation(paramTypes.get(i), paramNames.get(i), argValues.get(i)));
        }

        return new ExecutionInformation(signature.toShortString(), className, methodName, paramInfos);
    }

    static class MethodParameterInformation {
        public final String type;
        public final String argName;
        public final String val;

        public MethodParameterInformation(String type, String argName, String val) {
            this.type = type;
            this.argName = argName;
            this.val = val;
        }
    }
}
