package com.meeting.organizer.config;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.classify.Classifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialRandomBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RetryConfiguration {

    private final ApplicationContext applicationContext;
    @Value("${rest-client.retries.delay-timeout}")
    private long delay;
    @Value("${rest-client.retries.number-of-attempts}")
    private int maxAttempts;
    @Value("${rest-client.retries.status-codes.idempotent-integrations}")
    private int[] retryIdempotentStatuses;
    @Value("${rest-client.retries.status-codes.non-idempotent-integrations}")
    private int[] retryNonIdempotentStatuses;
    @Value("${rest-client.retries.multiplier}")
    private float multiplier;

    @Bean("idempotentRetryTemplate")
    @Primary
    public RetryTemplate getIdempotentRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(getBackOffPolicy());
        retryTemplate.setRetryPolicy(new IntegrationsExceptionRetryPolicy(retryIdempotentStatuses));
        retryTemplate.setListeners(retryListeners());
        return retryTemplate;
    }

    private static RetryListener[] retryListeners() {
        return ImmutableList.of(new RetryListenerSupport() {

            @Override
            public <T, E extends Throwable> void onError(
                    RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                List<StackTraceElement> elements = new ArrayList<>(Throwables.lazyStackTrace(throwable));
                Collections.reverse(elements);
                List<String> rootCause = elements.stream()
                        .filter(stackTraceElement -> stackTraceElement.getClassName()
                                .contains("meeting-organizer"))
                        .map(StackTraceElement::getMethodName)
                        .collect(Collectors.toList());
                String method = !rootCause.isEmpty() ? rootCause.get(rootCause.size() - 1) : "";
                log.warn("Attempt #{} to access the resource '{}' has failed. Reason: {}", context.getRetryCount(),
                        method, throwable.toString());
            }
        }).toArray(new RetryListener[]{});
    }


    @Bean("nonIdempotentRetryTemplate")
    @Primary
    public RetryTemplate getNonIdempotentRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(getBackOffPolicy());
        retryTemplate.setRetryPolicy(new IntegrationsExceptionRetryPolicy(retryNonIdempotentStatuses));
        retryTemplate.setListeners(retryListeners());
        return retryTemplate;
    }

    private BackOffPolicy getBackOffPolicy() {
        final ExponentialRandomBackOffPolicy backOffPolicy = new ExponentialRandomBackOffPolicy();
        backOffPolicy.setInitialInterval(delay);
        long maxInterval = delay * BigDecimal.valueOf(multiplier).pow(maxAttempts - 1).longValue();
        backOffPolicy.setMaxInterval(maxInterval);
        backOffPolicy.setMultiplier(multiplier);
        return backOffPolicy;
    }

    class IntegrationsExceptionRetryPolicy extends ExceptionClassifierRetryPolicy {

        IntegrationsExceptionRetryPolicy(int[] retryCodes) {
            SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
            simpleRetryPolicy.setMaxAttempts(maxAttempts);
            setExceptionClassifier(classifiable -> {
                if (classifiable instanceof RestClientResponseException) {
                    int responseCode = ((RestClientResponseException) classifiable).getRawStatusCode();
                    if (Arrays.stream(retryCodes).anyMatch(value -> value == responseCode)) {
                        return simpleRetryPolicy;
                    }
                }

                return new NeverRetryPolicy();
            });
        }

        @Override
        public final void setExceptionClassifier(Classifier<Throwable, RetryPolicy> exceptionClassifier) {
            super.setExceptionClassifier(exceptionClassifier);
        }

        @Override
        public void registerThrowable(RetryContext context, Throwable throwable) {
            log.warn("Attempt {} to access the resource has failed. Reason: {}",
                    context.getRetryCount(), throwable.getMessage());
            super.registerThrowable(context, throwable);
        }
    }

}
