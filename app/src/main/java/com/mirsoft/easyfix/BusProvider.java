package com.mirsoft.easyfix;

import com.squareup.otto.Bus;

public class BusProvider {
    private static Bus instance = null;

    private BusProvider()
    {
        instance = new Bus();
    }

    public static Bus getInstance()
    {
        if(instance == null)
        {
            instance = new Bus();
        }
        return instance;
    }
}
