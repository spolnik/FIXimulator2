package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.InstrumentsApi;
import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.domain.Instrument;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;


public final class InstrumentRepository extends DefaultHandler implements InstrumentsApi {

    private final ArrayList<Instrument> instruments = new ArrayList<>();
    private final ArrayList<Instrument> oldInstruments = new ArrayList<>();
    private Callback callback = null;
    
    public InstrumentRepository(File file) {
        try {
            InputStream input = 
                    new BufferedInputStream(new FileInputStream(file));
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( input, this);		
        } catch ( Exception e ) {
            System.out.println( "Error reading/parsing instrument file." );
            e.printStackTrace();
        }
    }

    @Override
    public void reloadInstrumentSet( File file ) {
        try {
            oldInstruments.clear();
            oldInstruments.addAll(instruments);
            instruments.clear();
            InputStream input = 
                    new BufferedInputStream(new FileInputStream(file));
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( input, this);
            callback.update();
        } catch ( Exception e ) {
            System.out.println( "Error reading/parsing instrument file." );
            e.printStackTrace();
            instruments.clear();
            instruments.addAll(oldInstruments);
        }
    }
    
    @Override
    public void startElement( String namespace, String localName, 
                    String qualifiedName, Attributes attributes ){
        if ( qualifiedName.equals( "instrument" )) {
            String ticker = attributes.getValue( "ticker" );
            String cusip = attributes.getValue( "cusip" );
            String sedol = attributes.getValue( "sedol" );
            String name = attributes.getValue( "name" );
            String ric = attributes.getValue( "ric" );
            String price = attributes.getValue( "price" );
            Instrument instrument = 
                    new Instrument( ticker, sedol, name, ric, cusip, price );
            instruments.add( instrument );
        }
    }

    @Override
    public int size() {
        return instruments.size();
    }

    @Override
    public Instrument getInstrument( int i ) {
        return instruments.get( i );
    }

    @Override
    public Instrument getInstrument( String identifier ) {

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
    public Instrument randomInstrument() {
        Random generator = new Random();

        int size = instruments.size();
        int index = generator.nextInt( size );

        return instruments.get( index );
    }
	
    public void outputToXML() {
        try {
            BufferedWriter writer = 
                   new BufferedWriter(new FileWriter("config/instruments.xml"));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<instruments>\n");

            for (Instrument instrument : instruments) {
                String output = "   <instrument";
                output += " name=\"" + instrument.getName() + "\"";
                output += " ticker=\"" + instrument.getTicker() + "\"";
                output += " cusip=\"" + instrument.getCusip() + "\"";
                output += " sedol=\"" + instrument.getSedol() + "\"";
                output += " ric=\"" + instrument.getRIC() + "\"";
                output += " price=\"" + instrument.getPrice() + "\"";
                output += "/>\n";
                writer.write(output);
            }

            writer.write("</instruments>\n");
            writer.close();
        } catch ( IOException e ) {e.printStackTrace();}
    }

    @Override
    public void addCallback(Callback instrumentModel){
        this.callback = instrumentModel;
    }
}

