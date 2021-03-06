package com.wordpress.nprogramming.instruments.service;

import com.wordpress.nprogramming.instruments.api.InstrumentsRepository;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

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


        final InstrumentsRepository instrumentsRepository =
                new InMemoryInstrumentRepository(configuration);
        final InstrumentResource instrumentResource =
                new InstrumentResource(instrumentsRepository);
        final InstrumentsFileHealthCheck healthCheck =
                new InstrumentsFileHealthCheck(configuration.getDefaultInputFilePath());

        environment.healthChecks().register("inputFilePath", healthCheck);
        environment.jersey().register(instrumentResource);
    }
}
