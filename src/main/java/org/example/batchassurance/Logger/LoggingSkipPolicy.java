package org.example.batchassurance.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class LoggingSkipPolicy implements SkipPolicy {
    private static final Logger logger = LoggerFactory.getLogger(LoggingSkipPolicy.class);

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        logger.error("Skipping item due to exception: {}", t.getMessage(), t);
        return true;
    }
}