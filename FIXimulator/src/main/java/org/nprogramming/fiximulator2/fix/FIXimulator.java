package org.nprogramming.fiximulator2.fix;

import org.nprogramming.fiximulator2.api.InstrumentsApi;
import org.nprogramming.fiximulator2.api.OrdersApi;
import org.nprogramming.fiximulator2.api.RepositoryWithCallback;
import org.nprogramming.fiximulator2.core.LogMessageSet;
import org.nprogramming.fiximulator2.domain.Execution;
import org.nprogramming.fiximulator2.domain.IOI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import java.io.*;
import java.net.URL;

public final class FIXimulator {

    private static Logger LOG = LoggerFactory.getLogger(FIXimulator.class);

    private Acceptor acceptor = null;
    private static FIXimulatorApplication application = null;
    private static LogMessageSet messages = null;

    public FIXimulator(
            OrdersApi ordersApi,
            RepositoryWithCallback<Execution> executionRepository,
            RepositoryWithCallback<IOI> ioiRepository,
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
                    executionRepository,
                    ioiRepository,
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

