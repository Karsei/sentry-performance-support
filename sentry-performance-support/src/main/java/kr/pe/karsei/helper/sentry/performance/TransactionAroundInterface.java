package kr.pe.karsei.helper.sentry.performance;

import io.sentry.IHub;

public interface TransactionAroundInterface {
    /**
     * 메소드 실행 이전에 작동
     * @param iHub Sentry IHub
     * @param executionInformation 메소드 정보
     * @return 관리되고자 하는 객체
     */
    Object doBefore(IHub iHub, ExecutionInformation executionInformation);

    /**
     * 메소드 실행 이후에 작동
     * @param iHub Sentry IHub
     * @param executionInformation 메소드 정보
     * @param value 관리되고자 하는 객체
     */
    void doAfter(IHub iHub, ExecutionInformation executionInformation, Object value);

    /**
     * 메소드 실행 과정에서 예외 발생 시 작동
     * @param iHub Sentry IHub
     * @param executionInformation 메소드 정보
     * @param value 관리되고자 하는 객체
     * @param e 예외 객체
     */
    void doOnThrow(IHub iHub, ExecutionInformation executionInformation, Object value, Throwable e);
}
