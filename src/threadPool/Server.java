package threadPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {

    public static final Logger logger = Logger.getLogger(Server.class.getName());

    public class clientHandler implements Runnable{

        private Socket socket;

        clientHandler(Socket socket1){
            this.socket = socket1;
        }

        @Override
        public void run(){


            try {
                PrintWriter toClient = new PrintWriter(socket.getOutputStream(),true);
                toClient.println("Server saying hello!!");
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = fromClient.readLine();
                logger.info("Message from client: " + line);
                socket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {

        int port = 8010;
        Server server = new Server();

        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);

        ExecutorService pool = Executors.newFixedThreadPool(10);

        System.out.println("Server is listening at port: " + port);

        while(true){
            try{
                Socket acceptedConnection = serverSocket.accept();
                logger.info("Server Socket connected to: "+ acceptedConnection.getRemoteSocketAddress().toString());
                pool.submit(server.new clientHandler(acceptedConnection));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
