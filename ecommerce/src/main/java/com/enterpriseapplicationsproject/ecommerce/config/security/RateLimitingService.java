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

    // Buckets per gli IP
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

    // Buckets per gli utenti autenticati
    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    // Configurazione per il rate limit: numero massimo di richieste e finestra temporale
    private static final int REQUESTS_PER_MINUTE_IP = 80;
    private static final int REQUESTS_PER_MINUTE_USER = 10;

    // Metodo per ottenere un bucket basato su IP
    public Bucket resolveBucketForIp(String ip) {
        return ipBuckets.computeIfAbsent(ip, this::newIpBucket);
    }

    // Metodo per ottenere un bucket basato sull'ID utente (utente autenticato)
    public Bucket resolveBucketForUser(String userId) {
        return userBuckets.computeIfAbsent(userId, this::newUserBucket);
    }

    // Creazione del bucket per IP
    private Bucket newIpBucket(String ip) {
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_MINUTE_IP, Refill.greedy(REQUESTS_PER_MINUTE_IP, Duration.ofMinutes(1)));
        return builder().addLimit(limit).build();
    }

    // Creazione del bucket per utente autenticato
    private Bucket newUserBucket(String userId) {
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_MINUTE_USER, Refill.greedy(REQUESTS_PER_MINUTE_USER, Duration.ofMinutes(1)));
        return builder().addLimit(limit).build();
    }

    // Verifica se l'IP ha superato il limite di richieste
    public boolean tryAcquireForIp(String ip) {
        Bucket bucket = resolveBucketForIp(ip);
        return bucket.tryConsume(1);
    }

    // Verifica se l'utente autenticato ha superato il limite di richieste
    public boolean tryAcquireForUser(String userId) {
        Bucket bucket = resolveBucketForUser(userId);
        return bucket.tryConsume(1);
    }

    // Metodo generico per acquisire il rate limit su chiave (sia IP che utente)
    public boolean tryAcquire(String key, int limit, int timeWindowInSeconds) {
        Bucket bucket = userBuckets.computeIfAbsent(key, k -> createBucket(limit, timeWindowInSeconds)); // Se la chiave non esiste, crea un nuovo bucket
        return !bucket.tryConsume(1); //
    }

    // Metodo per creare un bucket personalizzato
    private Bucket createBucket(int limit, int timeWindowInSeconds) {
        Bandwidth bandwidth = Bandwidth.classic(limit, Refill.greedy(limit, Duration.ofSeconds(timeWindowInSeconds)));
        return builder().addLimit(bandwidth).build();
    }
}
