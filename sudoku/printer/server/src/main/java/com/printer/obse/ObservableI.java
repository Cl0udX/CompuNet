package com.printer.obse;

import java.util.ArrayList;
import java.util.HashMap;

import com.zeroc.Ice.Current;

import Demo.ObserverPrx;

public class ObservableI implements Demo.Subject{

    
    private HashMap<String, ObserverPrx> observers;
    private HashMap<String, ClientHandler> handlers;

    public ObservableI(){
        observers = new HashMap<>();
        handlers = new HashMap<>();
    }

    @Override
    public void registerObserver(String name, ObserverPrx o, Current current) {
        ObserverPrx prx = o.ice_fixed(current.con);
        observers.put(name, prx);
        handlers.put(name, new ClientHandler(prx));

        if (current.con != null){
            current.con.setCloseCallback(connection -> {
                System.out.println("Conexión cerrada, eliminando observer: " + prx.ice_getIdentity());
                observers.values().remove(prx);
            });
        }

        System.out.println("Registering observer: -> "+o.toString());
        
    }

    @Override
    public void removeObserver(String o, Current current) {
        observers.remove(o);
        ClientHandler handler = handlers.remove(o);
        if (handler != null) {
            handler.stopCounting();
        }
    }

    @Override
    public void startCounting(String name, Current current) {
        System.out.println("Starting count for observer: " + name);
        ClientHandler handler = handlers.get(name);
        if (handler != null) {
            handler.startCounting();
        }
    }

    @Override
    public void stopCounting(String name, Current current) {
        System.out.println("Stopping count for observer: " + name);
        ClientHandler handler = handlers.get(name);
        if (handler != null) {
            handler.stopCounting();
        }
    }

    @Override
    public void update(String s, Current current) {
        System.out.println("Updating observer: " + s);
    }

    public void notifyObservers(String s, String msg){
        ObserverPrx o = observers.get(s);
        o.update(msg);
    }

    public void notifyObservers(String mensaje){
        for (ObserverPrx o : observers.values()){
            o.update(mensaje);
        }
    }
    
}

