package com.intedu.proxy;

import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by SD on 05.02.2017.
 */
public class Server {

    private static final Logger log = Logger.getLogger(Server.class);
    public static void main(String[] args) {
        log.info("In the run method of ServerLauncher");
        int portNumber;
        int threadsNumber;
        if (args.length > 1){
            portNumber = Integer.parseInt(args[0]);
            threadsNumber = Integer.parseInt(args[1]);
        } else {
            portNumber = 8055;
            threadsNumber = 10;
        }
        ExecutorService pool = null;

        try(ServerSocket serverSocket = new ServerSocket(portNumber)){
            pool = Executors.newFixedThreadPool(threadsNumber);
            log.info(String.format("Server is online and listening on the port %d", portNumber));
            /**
             * Infinite loop creates Socket accepting connection and
             * passes it as an argument to new Runnable object TripHandler.
             * Runnable passed to one of the threads in the pool for execution.
             */
            while (true){
                Socket client = serverSocket.accept();
                Runnable worker = new RequestHandler(client);
                pool.execute(worker);
            }
        } catch (Exception e){
            log.error(String.format("Exception while trying to listen or listening to port : %d", portNumber));
            log.error(e);
        } finally {
            if (pool != null) {
                pool.shutdown();
            }
        }
    }
}
