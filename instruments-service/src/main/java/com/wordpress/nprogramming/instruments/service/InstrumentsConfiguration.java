package com.wordpress.nprogramming.instruments.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class InstrumentsConfiguration extends Configuration {

    @NotEmpty
    private String inputFilePath;

    @JsonProperty
    public String getDefaultInputFilePath() {
        return inputFilePath;
    }

    @JsonProperty
    public void setDefaultInputFilePath(String path) {
        this.inputFilePath = path;
    }
}
