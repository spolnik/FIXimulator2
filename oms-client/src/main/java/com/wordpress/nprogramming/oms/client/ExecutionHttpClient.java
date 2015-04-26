package com.wordpress.nprogramming.oms.client;

import com.wordpress.nprogramming.oms.api.Execution;

public class ExecutionHttpClient extends BaseHttpClient<Execution> {

    public ExecutionHttpClient() {
        super(Execution.class);
    }

    @Override
    protected String path() {
        return "api/executions";
    }
}
