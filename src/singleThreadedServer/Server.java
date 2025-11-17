package singleThreadedServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server{

    public static final Logger logger = Logger.getLogger(singleThreadedServer.Server.class.getName());

    public void run() throws IOException {

        int port = 8010;
        ServerSocket serverSocket = new ServerSocket(port);

        serverSocket.setSoTimeout(10000);
        System.out.println("Server is listening on port: " + port);
        while(true){
            try{

                Socket acceptedConnection = serverSocket.accept();
                logger.info("Server is connected to client : " + acceptedConnection.getRemoteSocketAddress());
                PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream(),true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream()));
                toClient.println("Hello from Server");
                toClient.flush();

                logger.info("Waiting for client message...");
                String clientMsg = fromClient.readLine();   // read from client
                logger.info("Client said: " + clientMsg);

                acceptedConnection.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args){

        Server server = new Server();
        try {
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}