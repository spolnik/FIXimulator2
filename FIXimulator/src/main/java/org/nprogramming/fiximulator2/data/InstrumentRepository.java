package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.InstrumentsApi;
import org.nprogramming.fiximulator2.domain.Instrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InstrumentRepository extends DefaultHandler implements InstrumentsApi {

    private final List<Instrument> instruments = new ArrayList<>();

    private static final Logger LOG = LoggerFactory.getLogger(InstrumentRepository.class);

    public InstrumentRepository(File file) {
        try {
            InputStream input =
                    new BufferedInputStream(new FileInputStream(file));
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(input, this);
        } catch (Exception e) {
            LOG.error("Error reading/parsing instrument file.", e);
        }
    }

    @Override
    public void startElement(String namespace, String localName,
                             String qualifiedName, Attributes attributes) {
        if (qualifiedName.equals("instrument")) {

            String ticker = attributes.getValue("ticker");
            String cusip = attributes.getValue("cusip");
            String sedol = attributes.getValue("sedol");
            String name = attributes.getValue("name");
            String ric = attributes.getValue("ric");
            String price = attributes.getValue("price");
            Instrument instrument =
                    new Instrument(ticker, sedol, name, ric, cusip, price);

            instruments.add(instrument);
        }
    }

    @Override
    public Instrument getInstrument(String identifier) {

        for (Instrument instrument : instruments) {
            if (instrument.getTicker().equals(identifier) ||
                    instrument.getSedol().equals(identifier) ||
                    instrument.getCusip().equals(identifier) ||
                    instrument.getName().equals(identifier))
                return instrument;
        }

        return null;
    }

    @Override
    public List<Instrument> getAll() {
        return Collections.unmodifiableList(instruments);
    }
}

