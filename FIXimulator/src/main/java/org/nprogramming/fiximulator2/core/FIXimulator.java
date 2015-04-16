package org.nprogramming.fiximulator2.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import org.nprogramming.fiximulator2.api.ExecutionsApi;
import org.nprogramming.fiximulator2.api.IndicationsOfInterestApi;
import org.nprogramming.fiximulator2.api.InstrumentsApi;
import org.nprogramming.fiximulator2.api.OrdersApi;
import org.nprogramming.fiximulator2.data.InstrumentRepository;
import org.nprogramming.fiximulator2.fix.OrderFixTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Acceptor;
import quickfix.CompositeLogFactory;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldConvertError;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.JdbcLogFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;

public final class FIXimulator {

    private static Logger LOG = LoggerFactory.getLogger(FIXimulator.class);

    private Acceptor acceptor = null;
    private static FIXimulatorApplication application = null;
    private static InstrumentRepository instruments = null;
    private static LogMessageSet messages = null;

    public FIXimulator(
            OrdersApi ordersApi,
            ExecutionsApi executionsApi,
            IndicationsOfInterestApi indicationsOfInterestApi,
            InstrumentsApi instrumentsApi
    ) {
        InputStream inputStream = null;
        ClassLoader classLoader = FIXimulator.class.getClassLoader();

        try {

            URL configUrl = classLoader.getResource("config/FIXimulator.cfg");

            inputStream = new BufferedInputStream(
                    new FileInputStream(
                            new File(configUrl.getPath())));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            System.exit(0);
        }

        messages = new LogMessageSet();
        try {
            SessionSettings settings = new SessionSettings(inputStream);

            application = new FIXimulatorApplication(
                    settings,
                    messages,
                    ordersApi,
                    executionsApi,
                    indicationsOfInterestApi,
                    instrumentsApi,
                    new OrderFixTranslator(ordersApi)
            );

            MessageStoreFactory messageStoreFactory =
                    new FileStoreFactory(settings);
            boolean logToFile = false;
            boolean logToDB = false;
            LogFactory logFactory;
            try {
                logToFile = settings.getBool("FIXimulatorLogToFile");
                logToDB = settings.getBool("FIXimulatorLogToDB");
            } catch (FieldConvertError ex) {
                LOG.error("Error: ", ex);
            }
            if (logToFile && logToDB) {
                logFactory = new CompositeLogFactory(
                        new LogFactory[]{new ScreenLogFactory(settings),
                                new FileLogFactory(settings),
                                new JdbcLogFactory(settings)});
            } else if (logToFile) {
                logFactory = new CompositeLogFactory(
                        new LogFactory[]{new ScreenLogFactory(settings),
                                new FileLogFactory(settings)});
            } else if (logToDB) {
                logFactory = new CompositeLogFactory(
                        new LogFactory[]{new ScreenLogFactory(settings),
                                new JdbcLogFactory(settings)});
            } else {
                logFactory = new ScreenLogFactory(settings);
            }
            MessageFactory messageFactory = new DefaultMessageFactory();
            acceptor = new SocketAcceptor
                    (application, messageStoreFactory,
                            settings, logFactory, messageFactory);
        } catch (ConfigError e) {
            LOG.error("ConfigError: ", e);
        }
    }

    public static FIXimulatorApplication getApplication() {
        return application;
    }

    public static LogMessageSet getMessageSet() {
        return messages;
    }

    public void start() {
        try {
            acceptor.start();
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
    }
}

