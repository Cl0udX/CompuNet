package co.icesi.sudoku;

import co.icesi.sudoku.controllers.TCPController;
import co.icesi.sudoku.services.ServicesImpl;

public class Main {
    
    private ServicesImpl services;
    private TCPController tcpController;

    public static void main(String[] args) {
        System.out.println("Sudoku Game Initialized");
        ServicesImpl services = new ServicesImpl();
        TCPController tcpController = new TCPController(services);
        new Thread(() -> {
            tcpController.startService();
        }).start();
    }
}
