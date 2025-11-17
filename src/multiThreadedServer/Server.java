package multiThreadedServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Server {

    public static final Logger logger = Logger.getLogger(Server.class.getName());

    public Consumer<Socket> getConsumer() {
        //clientSocket -> socket connection of server with that particular client. Server can have multiple clientSocket

        return new Consumer<Socket>(){
            @Override
            public void accept(Socket clientSocket){
                try {
                    PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    logger.info("Server is connected to client : " + clientSocket.getRemoteSocketAddress());
                    toClient.println("Hello from Server");

                    String clientMsg = fromClient.readLine();   // read from client
                    logger.info("Client said: " + clientMsg);

                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };

        /*return (clientSocket) -> {
            try {
                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                toClient.println("Hello from Server");
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };*/
    }

    public static void main(String[] args) throws IOException {

        int port = 8010;
        Server server = new Server();

            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);
        System.out.println("Server is listening on port: " + port);
            while (true) {
                try{

                    Socket acceptedConnection = serverSocket.accept();
                    Thread thread = new Thread(() -> server.getConsumer().accept(acceptedConnection));
                    thread.start();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

    }
}
