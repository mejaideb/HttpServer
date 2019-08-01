package com.tavisca.workshops.httpserver;

import java.io.*;
import java.net.Socket;

public class HandleMultipleClientRequest extends Thread {


    private Socket socket;

    public HandleMultipleClientRequest(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {

        try (BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
             BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {

            parseClientRequestToGetDesiredFileRequested(in, out);
            closeSocketAfterEachRequest();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readClientRequest(BufferedInputStream in) throws IOException {
        byte b[] = new byte[256];
        in.read(b);
        return new String(b);
    }

    private void parseClientRequestToGetDesiredFileRequested(BufferedInputStream in, BufferedOutputStream out) throws IOException {
        String line = readClientRequest(in);

        String[] parse = line.split(" ");
        String getRequestName = parse[0];
        String filename = parse[1].replace("/", "");

        respondToTheGivenInputByTheClient(out, getRequestName, filename);
        out.flush();
    }

    private void respondToTheGivenInputByTheClient(BufferedOutputStream out, String getRequestName, String filename) throws IOException {
        if (getRequestName.equals("GET")) {
            Response response = new Response();
            response.sendResponse(out, filename);

        }

    }

    private void closeSocketAfterEachRequest() throws IOException {
        socket.close();
    }
}

