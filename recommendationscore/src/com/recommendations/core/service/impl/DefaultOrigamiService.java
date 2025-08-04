package com.recommendations.core.service.impl;

import com.recommendations.core.service.OrigamiService;

import java.util.Set;

public class DefaultOrigamiService implements OrigamiService {
    @Override
    public Set<String> getAvailableStrategies() {
        return Set.of("example_strategy_1", "example_strategy_2", "example_strategy_3");
    }
}
