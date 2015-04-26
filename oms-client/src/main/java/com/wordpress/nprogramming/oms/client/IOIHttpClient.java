package com.wordpress.nprogramming.oms.client;

import com.wordpress.nprogramming.oms.api.IOI;

public class IOIHttpClient extends BaseHttpClient<IOI> {

    public IOIHttpClient() {
        super(IOI.class);
    }

    @Override
    protected String path() {
        return "api/ioi";
    }

    public static void main(String[] args) {
        IOIHttpClient httpClient = new IOIHttpClient();

        String id = "IBM";
        IOI ioiToSave = new IOI(id);
        ioiToSave.setIDSource("TICKER");
        ioiToSave.setPrice(2.22);
        httpClient.save(ioiToSave);

        IOI ioi = httpClient.queryById(id);

        System.out.println("ioi('" + id + "')");
        System.out.println(ioi.toString());
    }
}
