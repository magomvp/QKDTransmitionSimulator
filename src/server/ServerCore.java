package server;

import BB84Protocol.BB84Protocol;
import java.io.*;
import java.net.*;

/**
 * Open and wait for connections on PORT (default 8080)
 * @author 
 */
public class ServerCore extends Thread {

    public static final int PORT = 7;
    private BB84Protocol _protocol;

    @Override
    public void run() {
        ServerSocket server_socket = null;
        try {
            server_socket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT + ".");

            while (true) {
                Socket socket = server_socket.accept();

                System.out.println("Client " + socket.getInetAddress() + " accepted\n");

                _protocol = new BB84Protocol(socket, false);
            }

        } catch (IOException ex) {
            System.err.println("Error :" + ex.getMessage());
        } finally {
            try {
                server_socket.close();
                System.out.println("closing...");
            } catch (IOException ex2) {
                System.err.println("Error :" + ex2.getLocalizedMessage());
            }
        }
    }
}
