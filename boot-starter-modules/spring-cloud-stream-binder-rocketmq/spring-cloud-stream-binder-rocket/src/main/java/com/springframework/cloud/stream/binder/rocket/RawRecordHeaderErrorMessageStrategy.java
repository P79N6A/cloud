package com.springframework.cloud.stream.binder.rocket;

import com.springframework.cloud.stream.binder.rocketmq.utils.CommonUtils;
import org.springframework.core.AttributeAccessor;
import org.springframework.integration.support.ErrorMessageStrategy;
import org.springframework.integration.support.ErrorMessageUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;

import java.util.Collections;
import java.util.Map;

/**
 * @author summer
 */
public class RawRecordHeaderErrorMessageStrategy implements ErrorMessageStrategy {

    @Override
    public ErrorMessage buildErrorMessage(Throwable payload, AttributeAccessor attributes) {
        Object inputMessage = attributes.getAttribute(ErrorMessageUtils.INPUT_MESSAGE_CONTEXT_KEY);
        Map<String, Object> headers = Collections.singletonMap(CommonUtils.ROCKETMQ_RAW_DATA,
                attributes.getAttribute(CommonUtils.ROCKETMQ_RAW_DATA));
        return inputMessage instanceof Message
                ? new ErrorMessage(payload, headers,
                (Message<?>) inputMessage)
                : new ErrorMessage(payload, headers);
    }

}