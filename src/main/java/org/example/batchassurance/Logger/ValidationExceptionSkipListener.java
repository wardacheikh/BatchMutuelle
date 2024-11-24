package org.example.batchassurance.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.SkipListenerSupport;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidationExceptionSkipListener extends SkipListenerSupport<Object, Object> {

    private static final Logger log = LoggerFactory.getLogger(ValidationExceptionSkipListener.class);

    @Override
    public void onSkipInProcess(Object item, Throwable t) {
        if (t instanceof ValidationException) {
            log.error("Validation exception occurred for item: {}", item, t);
        }
    }
}