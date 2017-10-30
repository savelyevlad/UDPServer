package server;

import client.Client;
import client.Id;
import client.Pair;
import client.Sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;

public class Server implements Runnable {

    private DatagramSocket socket;
    private byte[] buf = new byte[1 << 14];
    private final int PORT = 50000;

    private HashMap<Id, Client> clientHashMap = new HashMap<>();
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

        while(true) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                socket.receive(datagramPacket);

                Id id = new Id(datagramPacket.getAddress(), datagramPacket.getPort());

                if(clientHashMap.get(id) == null) {
                    Client client = new Client(id, clients.size(), datagramPacket.getAddress(), datagramPacket.getPort());
                    clientHashMap.put(id, client);
                    clients.add(client);
                }

                Integer number = (int) datagramPacket.getData()[0];

                // TODO: if(number < clients.size()) {}

                Client client = clients.get(number);
                Pair<DatagramPacket, Client> toSend = new Pair<>(datagramPacket, client);

                Thread thread = new Thread(new Sender(toSend, socket));
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
