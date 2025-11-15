package multiThreadedServer;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args){

        int port = 8010;

        try{
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }
}
