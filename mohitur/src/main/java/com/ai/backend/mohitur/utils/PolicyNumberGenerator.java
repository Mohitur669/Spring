package com.ai.backend.mohitur.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PolicyNumberGenerator {

    private static final String PREFIX = "POL";
    private static final AtomicLong counter = new AtomicLong(1000);

    public static String generate() {
        long timestamp = Instant.now().toEpochMilli();
        long sequence = counter.getAndIncrement();
        return String.format("%s-%d-%06d", PREFIX, timestamp % 1000000, sequence);
    }
}