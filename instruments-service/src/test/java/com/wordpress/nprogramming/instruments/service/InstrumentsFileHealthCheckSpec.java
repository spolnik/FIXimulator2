package com.wordpress.nprogramming.instruments.service;

import com.codahale.metrics.health.HealthCheck;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentsFileHealthCheckSpec {

    @Test
    public void returnsHealthyStatusIfPathExists() throws Exception {
        InstrumentsFileHealthCheck healthCheck =
                new InstrumentsFileHealthCheck("instruments.json");

        HealthCheck.Result result = healthCheck.check();

        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void returnsUnhealthyStatusIfPathIsWrong() throws Exception {
        InstrumentsFileHealthCheck healthCheck =
                new InstrumentsFileHealthCheck("instruments.dummy");

        HealthCheck.Result result = healthCheck.check();

        assertThat(result.isHealthy()).isFalse();
    }
}