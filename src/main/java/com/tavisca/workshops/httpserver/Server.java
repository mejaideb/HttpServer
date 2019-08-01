package com.tavisca.workshops.httpserver;

// A Java program for a Server

import java.net.*;
import java.io.*;

public class Server {

    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private boolean isAcceptingMoreClient = true;

    public Server(int port) throws IOException, InterruptedException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        while (isAcceptingMoreClient) {

            acceptingClients();
            threadProcess();
        }
    }

    private void threadProcess() throws InterruptedException {
        Thread t = new HandleMultipleClientRequest(socket);
        System.out.println("Thread Starting..");
        t.start();
        //t.join(); -- thread join stops the running thread and keeps waiting the running thread..until it completes itself..
        System.out.println("Thread finished ..");
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
