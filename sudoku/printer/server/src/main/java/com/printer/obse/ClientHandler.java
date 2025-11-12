package com.printer.obse;

import Demo.ObserverPrx;

public class ClientHandler {
    private final ObserverPrx observer;
    private Thread counterThread;
    private volatile boolean running = false;

    public ClientHandler(ObserverPrx observer) {
        this.observer = observer;
    }

    public void startCounting() {
        int interval = 10;
        if (running)
            return;
        running = true;
        counterThread = new Thread(() -> {
            int count = 0;
            try {
                while (running) {
                    observer.updateCount(count);
                    count+=interval;
                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        counterThread.start();
    }

    public void stopCounting() {
        running = false;
        if (counterThread != null) {
            counterThread.interrupt();
        }
    }

}
