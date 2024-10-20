package com.enterpriseapplicationsproject.ecommerce.config.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    // Buckets per gli IP
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

    // Buckets per gli utenti autenticati
    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    private final Map<String, Integer> bucketLimits = new ConcurrentHashMap<>();
    private final Map<String, Integer> bucketTimeWindows = new ConcurrentHashMap<>();

    // Configurazione predefinita per il rate limit: numero massimo di richieste e finestra temporale
    private static final int DEFAULT_REQUESTS_PER_IP = 12;
    private static final int DEFAULT_REQUESTS_PER_USER = 35;
    private static final int DEFAULT_TIME_WINDOW_IN_SECONDS = 45; // 1 minuto in secondi

    // Metodo per ottenere un bucket basato su IP (usando i valori predefiniti)
    public Bucket resolveBucketForIp(String ip) {
        return ipBuckets.computeIfAbsent(ip, this::newIpBucket);
    }

    // Metodo per ottenere un bucket basato sull'ID utente (usando i valori predefiniti)
    public Bucket resolveBucketForUser(String userId) {
        return userBuckets.computeIfAbsent(userId, this::newUserBucket);
    }

    // Creazione del bucket per IP con il limite predefinito
    private Bucket newIpBucket(String ip) {
        Bandwidth limit = Bandwidth.classic(DEFAULT_REQUESTS_PER_IP, Refill.greedy(DEFAULT_REQUESTS_PER_IP, Duration.ofSeconds(DEFAULT_TIME_WINDOW_IN_SECONDS)));
        return Bucket.builder().addLimit(limit).build();
    }

    // Creazione del bucket per utente con il limite predefinito
    private Bucket newUserBucket(String userId) {
        Bandwidth limit = Bandwidth.classic(DEFAULT_REQUESTS_PER_USER, Refill.greedy(DEFAULT_REQUESTS_PER_USER, Duration.ofSeconds(DEFAULT_TIME_WINDOW_IN_SECONDS)));
        return Bucket.builder().addLimit(limit).build();
    }

    // Verifica se l'IP ha superato il limite di richieste (usando i valori predefiniti)
    public boolean tryAcquireForIp(String ip) {
        Bucket bucket = resolveBucketForIp(ip);
        return bucket.tryConsume(1);  // Consuma un token
    }

    // Verifica se l'utente autenticato ha superato il limite di richieste (usando i valori predefiniti)
    public boolean tryAcquireForUser(String userId) {
        Bucket bucket = resolveBucketForUser(userId);
        return bucket.tryConsume(1);  // Consuma un token
    }

    // Metodo generico per acquisire il rate limit con parametri personalizzati (se forniti)
    public boolean tryAcquire(String key, Integer limit, Integer timeWindowInSeconds) {
        int effectiveLimit = (limit != null) ? limit : DEFAULT_REQUESTS_PER_USER;
        int effectiveTimeWindow = (timeWindowInSeconds != null) ? timeWindowInSeconds : DEFAULT_TIME_WINDOW_IN_SECONDS;

        // Controlla se esiste già un bucket per la chiave
        Bucket existingBucket = userBuckets.get(key);
        Integer currentLimit = bucketLimits.get(key);
        Integer currentTimeWindow = bucketTimeWindows.get(key);

        // Verifica se il bucket esistente ha limiti diversi da quelli passati
        if (existingBucket == null || !(effectiveLimit == currentLimit) || ! (effectiveTimeWindow == currentTimeWindow)) {
            // Crea un nuovo bucket con i limiti aggiornati
            Bucket newBucket = createBucket(effectiveLimit, effectiveTimeWindow);
            userBuckets.put(key, newBucket);
            bucketLimits.put(key, effectiveLimit);
            bucketTimeWindows.put(key, effectiveTimeWindow);
        }

        // Usa il bucket attuale
        Bucket bucket = userBuckets.get(key);

        // Stampa i limiti attuali per debug
        System.out.println("Actual Effective Limit: " + bucketLimits.get(key));
        System.out.println("Actual Effective Time Window: " + bucketTimeWindows.get(key));

        // Consuma un token dal bucket
        return bucket.tryConsume(1);  // Consuma un token
    }

    // Metodo per creare un bucket con limiti personalizzati o predefiniti
    private Bucket createBucket(int limit, int timeWindowInSeconds) {
        Bandwidth bandwidth = Bandwidth.classic(limit, Refill.greedy(limit, Duration.ofSeconds(timeWindowInSeconds)));
        return Bucket.builder().addLimit(bandwidth).build();
    }
}

