package com.tavisca.workshops.httpserver;

import java.io.*;
import java.net.Socket;


public class HandleMultipleClientRequest extends Thread {
    Socket socket;
    BufferedInputStream in;
    BufferedOutputStream out;
    PrintWriter printWriter;
    FileReader fileReader;
    BufferedReader br;


    public HandleMultipleClientRequest(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedInputStream(socket.getInputStream());
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void closeSocketAfterEachRequest() throws IOException {
        socket.close();
    }

    private void parseClientRequestToGetDesiredFileRequested(BufferedInputStream in, BufferedOutputStream out) throws IOException {
        String line = readClientRequest(in);

        String[] parse = line.split(" ");
        String getRequestName = parse[0];
        String filename = parse[1].replace("/", "");

        if (getRequestName.equals("GET")) {
            Response response = new Response();
            response.sendResponse(out, filename);

        }
        out.flush();
    }

    private String readClientRequest(BufferedInputStream in) throws IOException {
        byte b[] = new byte[256];
        in.read(b);
        return new String(b);
    }
}

