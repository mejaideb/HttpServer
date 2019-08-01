package com.tavisca.workshops.httpserver;

import java.io.*;
import java.util.Date;

public class Response {

    public void sendResponse(BufferedOutputStream out, String filename) {

        readContentsOfFiles(out, filename);


    }

    private void readContentsOfFiles(BufferedOutputStream out, String filename) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);

            attachHeaders(out);
            redirectDataToBrowser(out, fileData);
        }

        catch (FileNotFoundException e) {
            handleFileNotFound(out, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFileNotFound(BufferedOutputStream out, String filename) {
        if (filename.trim().isEmpty())
            sendResponse(out, "index.html");
        else
            sendResponse(out, "filenotfound.html");
    }

    private void redirectDataToBrowser(BufferedOutputStream out, byte[] fileData) throws IOException {
        String contentLength = "Content-Length: " + fileData.length + "\n";
        out.write(contentLength.getBytes());
        out.write("\n".getBytes());
        out.write(fileData);
    }

    private void attachHeaders(BufferedOutputStream out) throws IOException {
        out.write("HTTP/1.1 200 OK\n".getBytes());
        out.write("Content-type: text\n".getBytes());
        out.write("Server: MyServer\n".getBytes());
    }
}
