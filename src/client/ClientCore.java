package client;

import BB84Protocol.BB84Protocol;
import java.io.*;
import java.net.*;

/**
 *
 * @author mago
 */
public class ClientCore {

    private Socket socket = null;
    private BB84Protocol _protocol;
    

    public ClientCore(String serverAddress, int serverSocket) throws UnknownHostException, IOException {

        InetAddress adress = InetAddress.getByName(serverAddress);
        socket = new Socket(adress, serverSocket);

        _protocol = new BB84Protocol(socket, true);
    }
}

