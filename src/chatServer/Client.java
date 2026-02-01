package chatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        int port = 8010;
        InetAddress address = InetAddress.getByName("localhost");
        Socket socket = new Socket(address, port);
        System.out.println("Connected to chat");

            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader fromUserKeyboard = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter toSocket = new PrintWriter(socket.getOutputStream(),true);

            //thrad to listen server mssg
            Thread thread = new Thread(()->{
                try{
                    String mssg;
                    while ((mssg = fromServer.readLine()) != null ){
                        System.out.println(">>>"+mssg);
                    }
                }catch (Exception ex){
                    System.out.println("Connetion closed");
                }
            });
            thread.start();

            String input;
            while ((input = fromUserKeyboard.readLine()) != null ){
                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Disconnecting...");
                    toSocket.println("has left the chat.");
                    socket.close();       // <-- IMPORTANT
                    break;
                }

                toSocket.println(input);
            }

            thread.join();

    }
}
