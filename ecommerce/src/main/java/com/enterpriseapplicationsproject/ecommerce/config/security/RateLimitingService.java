package com.enterpriseapplicationsproject.ecommerce.config.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.bucket4j.Bucket.builder;

@Service
public class RateLimitingService {

    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    private static final int REQUESTS_PER_MINUTE_IP = 30;
    private static final int REQUESTS_PER_MINUTE_USER = 45;

    public Bucket resolveBucketForIp(String ip) {
        return ipBuckets.computeIfAbsent(ip, this::newIpBucket);
    }

    public Bucket resolveBucketForUser(String userId) {
        return userBuckets.computeIfAbsent(userId, this::newUserBucket);
    }

    private Bucket newIpBucket(String ip) {
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_MINUTE_IP, Refill.greedy(REQUESTS_PER_MINUTE_IP, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket newUserBucket(String userId) {
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_MINUTE_USER, Refill.greedy(REQUESTS_PER_MINUTE_USER, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    public boolean tryAcquireForIp(String ip) {
        Bucket bucket = resolveBucketForIp(ip);
        return bucket.tryConsume(1);  // Modifica qui: restituisce true se il token è consumato
    }

    public boolean tryAcquireForUser(String userId) {
        Bucket bucket = resolveBucketForUser(userId);
        return bucket.tryConsume(1);  // Modifica qui: restituisce true se il token è consumato
    }

    public boolean tryAcquire(String key, int limit, int timeWindowInSeconds) {
        Bucket bucket = userBuckets.computeIfAbsent(key, k -> createBucket(limit, timeWindowInSeconds));
        return bucket.tryConsume(1);
    }

    private Bucket createBucket(int limit, int timeWindowInSeconds) {
        Bandwidth bandwidth = Bandwidth.classic(limit, Refill.greedy(limit, Duration.ofSeconds(timeWindowInSeconds)));
        return Bucket.builder().addLimit(bandwidth).build();
    }
}
