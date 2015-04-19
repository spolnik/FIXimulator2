package com.wordpress.nprogramming.instruments.service;

import com.google.common.base.Preconditions;
import com.wordpress.nprogramming.instruments.api.InstrumentsApi;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import java.io.File;
import java.net.URL;

public class InstrumentsApplication extends Application<InstrumentsConfiguration> {

    public static void main(String[] args) throws Exception {
        new InstrumentsApplication().run(args);
    }

    @Override
    public String getName() {
        return "instruments-service";
    }

    @Override
    public void run(
            InstrumentsConfiguration configuration, Environment environment
    ) throws Exception {

        InstrumentsApi instrumentsApi = instrumentApi(
                configuration
        );

        final InstrumentResource instrumentResource = new InstrumentResource(instrumentsApi);
        final InstrumentsResource instrumentsResource = new InstrumentsResource(instrumentsApi);

        final InstrumentsFileHealthCheck healthCheck =
                new InstrumentsFileHealthCheck(configuration.getDefaultInputFilePath());

        environment.healthChecks().register("inputFilePath", healthCheck);

        environment.jersey().register(instrumentResource);
        environment.jersey().register(instrumentsResource);
    }

    private InstrumentsApi instrumentApi(InstrumentsConfiguration configuration) {

        URL instrumentsXml =
                InstrumentsApplication.class.getClassLoader().getResource(
                        configuration.getDefaultInputFilePath()
                );

        Preconditions.checkNotNull(
                instrumentsXml,
                configuration.getDefaultInputFilePath() + " does not exist."
        );

        return new InstrumentRepository(new File(instrumentsXml.getPath()));
    }
}
