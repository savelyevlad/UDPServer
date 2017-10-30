package server;

import client.Client;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;

public class Server implements Runnable {

    private DatagramSocket socket;
    private byte[] buf = new byte[16384];
    private final int PORT = 50000;

    private LinkedList<Client> clients = new LinkedList<>();

    public Server() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server was started");
        while (true) {

        }
    }
}
