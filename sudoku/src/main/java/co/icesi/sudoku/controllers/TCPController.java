package co.icesi.sudoku.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.icesi.sudoku.controllers.dtos.CellDTO;
import co.icesi.sudoku.controllers.dtos.Request;
import co.icesi.sudoku.controllers.dtos.Response;
import co.icesi.sudoku.model.Cell;
import co.icesi.sudoku.services.ServicesImpl;

public class TCPController {

    private ServicesImpl services;

    private ServerSocket serverSocket;

    private boolean running;

    private Executor executor;

    private Gson gson;

    public TCPController(ServicesImpl services) {
        this(services, 12345);
    }

    public TCPController(ServicesImpl services, int port) {
        this.services = services;
        try {
            serverSocket = new ServerSocket(port);
            executor = Executors.newFixedThreadPool(5);
            gson = new GsonBuilder().create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        running = true;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void startService() {
        System.out.println("TCP Service started on port " + serverSocket.getLocalPort());
        while (running) {
            try {
                executor.execute(new TCPClientHandler(serverSocket.accept(), services));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TCPClientHandler implements Runnable {

        private Socket clientSocket;
        private ServicesImpl services;

        public TCPClientHandler(Socket clientSocket, ServicesImpl services) {
            this.clientSocket = clientSocket;
            this.services = services;
        }

        @Override
        public void run() {
            try {
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String line = reader.readLine();
                Request rq = gson.fromJson(line, Request.class);
                Map<String, String> data = rq.data;
                Response response = new Response();
                response.data = new HashMap<>();
                CellDTO[][] board;
                switch (rq.action) {
                    case "SET_CELL":
                        int i = Integer.parseInt(data.get("i"));
                        int j = Integer.parseInt(data.get("j"));
                        int value = Integer.parseInt(data.get("value"));
                        try {
                            services.setValue(i, j, value);
                            response.status = "OK";
                            response.data.put("cellValue", value);
                            
                            response.data.put("gameEnd", false);
                        } catch (Exception e) {
                            response.data.put("gameEnd", true);
                            response.data.put("win", false);

                        }
                        board = services.getBoardCurrent();
                        response.data.put("board", board);
                        break;
                    case "SURRENDER":
                        services.surrender();
                        board = services.getBoardCurrent();
                        response.status = "OK";
                        response.data.put("board", board);
                        response.data.put("gameEnd", true);
                        response.data.put("win", false);
                        break;
                    case "VALIDATE_GAME":
                        boolean isValidSolution = services.isValidSolution();
                        boolean isComplete = services.isComplete();
                        response.status = "OK";
                        response.data.put("gameEnd", isComplete);
                        response.data.put("win", isValidSolution);
                        board = services.getBoardCurrent();
                        response.data.put("board", board);
                        break;
                    case "GET_BOARD":
                        board = services.getBoardCurrent();
                        response.status = "OK";
                        response.data.put("board", board);
                        break;
                    case "INIT_GAME":
                        services.initGame();
                        board = services.getBoardCurrent();
                        response.status = "OK";
                        response.data.put("board", board);
                        break;

                    default:
                        break;
                }

                String json = gson.toJson(response);
                writer.write(json);
                writer.newLine();
                writer.flush();
                writer.close();
                reader.close();

                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}