package com.printer.obse;


public class ObserverImpl implements Demo.Observer {

    @Override
    public void update(String s, com.zeroc.Ice.Current current) {
        System.out.println(s);
    }
    @Override
    public void updateCount(int count, com.zeroc.Ice.Current current) {
        System.out.println("Count from server: " + count);
    }
}


