package com.intedu.proxy;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by SD on 05.02.2017.
 */
public class RequestHandler implements Runnable {

    private final Logger logger = Logger.getLogger(RequestHandler.class);
    private Socket client;

    public RequestHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        String input = null;
        String url = null;
        InputStream serverResponse = null;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream reply = client.getOutputStream()) {
            input = reader.readLine();
            logger.debug(input);
            url = input.split("\\s")[1];
            logger.debug(url);
            URL targetUrl = new URL(url);
            URLConnection connection = targetUrl.openConnection();
            serverResponse = connection.getInputStream();
            IOUtils.copyLarge(serverResponse, reply);
            serverResponse.close();
        } catch (IOException e) {
            logger.error(String.format("Failed to forward request to %1$s. Exception : %2$s", input, e));
        }
    }
}
