package com.wordpress.nprogramming.instruments.service;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentsConfigurationSpec {

    private InstrumentsConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new InstrumentsConfiguration();
    }

    @Test
    public void setsProperlyPath() throws Exception {
        configuration.setDefaultInputFilePath("path");

        assertThat(configuration.getDefaultInputFilePath()).isEqualTo("path");
    }
}