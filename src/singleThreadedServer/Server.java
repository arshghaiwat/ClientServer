package singleThreadedServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    public void run() throws IOException {

        int port = 8010;
        ServerSocket serverSocket = new ServerSocket(port);

        serverSocket.setSoTimeout(10000);

        while(true){
            try{
                System.out.println("Server is listening on port: " + port);
                Socket acceptedConnection = serverSocket.accept();
                System.out.println("Server is connected to client : " + acceptedConnection.getRemoteSocketAddress());
                PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream(),true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream()));
                toClient.println("Hello from Server");
                toClient.flush();

                String clientMsg = fromClient.readLine();   // read from client
                System.out.println("Client said: " + clientMsg);

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