package com.tavisca.workshops.httpserver;

// A Java program for a Server

import java.net.*;
import java.io.*;

public class Server {

    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private boolean isAcceptingMoreClient = true;

    public Server(int port) throws IOException, InterruptedException {
        serverSocket(port);
        while (isAcceptingMoreClient) {
            acceptingClients();
            clientThreadStarted();
        }
    }

    private void serverSocket(int port) throws IOException {

        serverSocket = new ServerSocket(port);

    }

    private void clientThreadStarted() throws InterruptedException {
        Thread t = new HandleMultipleClientRequest(socket);
        t.start();

    }

    private void acceptingClients() throws IOException {
        System.out.println("Waiting for a client ...");
        socket = serverSocket.accept();
        System.out.println("Client accepted");
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        Server server = new Server(4444);
    }
}
