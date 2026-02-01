package chatServer;

import jdk.jfr.Experimental;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Server {

    public static final Logger logger = Logger.getLogger(Server.class.getName());
    public static final Map<PrintWriter, String> clients = new ConcurrentHashMap<>();
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");


    public static void handler(Socket socket){

        PrintWriter toClient = null;

        try{
            toClient = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            toClient.println("Enter username: ");
            String username = fromClient.readLine().trim();

            clients.put(toClient, username);

            //broadcast mssg to all users.
            broadcast(username+" has joined the chat");

            //Listens for messages from this client
            String message;
            while ((message = fromClient.readLine())!= null){
                String timestamp = LocalTime.now().format(timeFormat);
                broadcast("["+timestamp+"]"+username+": "+ message);
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            removeClient(toClient, socket);
        }

    }

    public static void broadcast(String message){
        for(PrintWriter writer: clients.keySet()){
            writer.println(message);
        }
    }

    public static void removeClient(PrintWriter writer, Socket socket){
        try {
            String username = clients.remove(writer);

            if(username != null){
                broadcast(username + " left the chat");
            }
            socket.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {

        int port = 8010;
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        System.out.println("Server is listening on port: "+ port);

        while(true){
            try{
                Socket acceptedConnection = serverSocket.accept();
                logger.info("Server Socket is connected to: "+ acceptedConnection.getRemoteSocketAddress());
                pool.submit(()->handler(acceptedConnection));
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }

    }

}
