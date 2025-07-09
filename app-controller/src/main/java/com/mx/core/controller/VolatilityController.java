package com.mx.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mx.core.service.VolatilityService;

@RestController
@RequestMapping("/volatility")
public class VolatilityController {

    private final VolatilityService volatilityService;

    public VolatilityController(VolatilityService volatilityService) {
        this.volatilityService = volatilityService;
    }

    @GetMapping
    public Map<String, Object> getVolatility(
            @RequestParam String symbol,
            @RequestParam String interval,
            @RequestParam(defaultValue = "50") int limit) {

        double vol = volatilityService.calculateVolatility(symbol.toUpperCase(), interval, limit);
        return Map.of(
                "symbol", symbol,
                "interval", interval,
                "limit", limit,
                "volatility", vol
        );
    }
    
    @GetMapping("/closes")
    public Map<String, Object> getCloses(
        @RequestParam String symbol,
        @RequestParam String interval,
        @RequestParam(defaultValue = "50") int limit
    ) {
        List<Double> closes = volatilityService.getClosingPrices(symbol.toUpperCase(), interval, limit);
        return Map.of(
            "symbol", symbol,
            "interval", interval,
            "limit", limit,
            "closes", closes
        );
    }
    
}