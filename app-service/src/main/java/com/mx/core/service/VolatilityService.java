package com.mx.core.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class VolatilityService {

    private static final String BINANCE_URL = "https://api.binance.com/api/v3/klines";
    private final RestTemplate restTemplate = new RestTemplate();

    public double calculateVolatility(String symbol, String interval, int limit) {
    	
        URI uri = UriComponentsBuilder.fromHttpUrl(BINANCE_URL)
            .queryParam("symbol", symbol)
            .queryParam("interval", interval)
            .queryParam("limit", limit)
            .encode(StandardCharsets.UTF_8)
            .build()
            .toUri();

        ResponseEntity<List<List<Object>>> response = restTemplate.exchange(
        	    uri,
        	    HttpMethod.GET,
        	    null,
        	    new ParameterizedTypeReference<List<List<Object>>>() {}
        	);
        
        List<?> klinesRaw = response.getBody();
        if (klinesRaw == null || klinesRaw.isEmpty()) {
        	return 0.0;
        }

        List<List<Object>> klines = new ArrayList<>();

        for (Object item : klinesRaw) {
            if (item instanceof List<?>) {
                List<?> rawList = (List<?>) item;
                klines.add(new ArrayList<>(rawList));
            }
        }
        
        List<Double> closes = new ArrayList<>();
        for (List<Object> kline : klines) {
            try {
                closes.add(Double.parseDouble(kline.get(4).toString())); // Índice 4: precio de cierre
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        if (closes.size() < 2) return 0.0;

        List<Double> logReturns = new ArrayList<>();
        for (int i = 1; i < closes.size(); i++) {
            double r = Math.log(closes.get(i) / closes.get(i - 1));
            logReturns.add(r);
        }

        double avg = logReturns.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        
        double variance = logReturns.stream()
            .mapToDouble(r -> Math.pow(r - avg, 2))
            .average().orElse(0.0);

        return Math.sqrt(variance); // Volatilidad histórica
        
    }
    
    public List<Double> getClosingPrices(String symbol, String interval, int limit) {
    	
        URI uri = UriComponentsBuilder.fromHttpUrl(BINANCE_URL)
            .queryParam("symbol", symbol)
            .queryParam("interval", interval)
            .queryParam("limit", limit)
            .encode(StandardCharsets.UTF_8)
            .build()
            .toUri();

        ResponseEntity<List<List<Object>>> response = restTemplate.exchange(
        	    uri,
        	    HttpMethod.GET,
        	    null,
        	    new ParameterizedTypeReference<List<List<Object>>>() {}
        	);
        
        List<?> klinesRaw = response.getBody();
        if (klinesRaw == null || klinesRaw.isEmpty()) return List.of();

        List<List<Object>> klines = new ArrayList<>();

        for (Object item : klinesRaw) {
            if (item instanceof List<?>) {
                List<?> rawList = (List<?>) item;
                klines.add(new ArrayList<>(rawList));
            }
        }
        
        List<Double> closes = new ArrayList<>();
        for (List<Object> kline : klines) {
            try {
                closes.add(Double.parseDouble(kline.get(4).toString()));
            } catch (Exception ignored) {}
        }

        return closes;
        
    }
    
}