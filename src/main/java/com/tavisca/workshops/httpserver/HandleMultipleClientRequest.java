package com.tavisca.workshops.httpserver;

import java.io.*;
import java.net.Socket;
import java.util.Date;


public class HandleMultipleClientRequest extends Thread {
    Socket socket;
    BufferedInputStream in;
    private static PrintWriter out;
    FileReader fileReader;
    BufferedReader br;


    public HandleMultipleClientRequest(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedInputStream(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            //respond extracted from client
            String line = respondFromClient();
            String filename;
            String requestType = line.split(" ")[0];
            if (requestType.equals("GET"))
            {
                filename = line.split(" ")[1].substring(1);
                System.out.println(filename);
                if (filename.trim().isEmpty()) {
                    fileNotFound("index.html");
                } else {
                    fileNotFound(filename);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String respondFromClient() throws IOException {
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        String line = new String(buffer);
        System.out.println(line);

        return line;
    }

    private void httpHeader(String responseString, int dataLength) {
        out.print("HTTP/1.1 200 OK\n");
        out.print("Date: " + new Date() + "\n");
        out.print("Server: MyServer\n");
        out.print("Content-Length: " + dataLength + "\n");
        out.print("Content-type: text/html\n");
        out.println();
        //System.out.println(responseString);
        out.print(responseString);
        out.flush();
        out.close();

    }

    private void fileNotFound(String filename) {
        try {
            File tempFile = new File(filename);
            boolean exists = tempFile.exists();
            System.out.println("file exists : " + exists);
            if (exists) {
                fileExists(filename);
            } else {
                fileDoesNotExists();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void fileDoesNotExists() throws IOException {
        String fileNotFound = "filenotfound.html";
        fileReader = new FileReader(fileNotFound);
        br = new BufferedReader(fileReader);

        String len;
        String responseStringFileNotFound = "";
        while ((len = br.readLine()) != null)
            responseStringFileNotFound += len + "\n";

        int dataLengthFileNotFound = responseStringFileNotFound.length();
        httpHeader(responseStringFileNotFound, dataLengthFileNotFound);
    }

    private void fileExists(String filename) throws IOException {
        fileReader = new FileReader(filename);
        br = new BufferedReader(fileReader);

        String l;
        String responseString = "";
        while ((l = br.readLine()) != null)
            responseString += l + "\n";

        int dataLength = responseString.length();


        System.out.println("See in Browser");
        httpHeader(responseString, dataLength);
    }
}

